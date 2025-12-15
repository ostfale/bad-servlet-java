package de.ostfale.va.adapter.out.filesystem;

import de.ostfale.va.application.domain.service.FilterTournamentsService;
import de.ostfale.va.application.port.out.DownloadFilePort;
import de.ostfale.va.common.FileSystemFacade;
import de.ostfale.va.common.UseCase;
import de.ostfale.va.common.UseLogging;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.Executors;

/**
 * Adapter for downloading files over HTTP/HTTPS using virtual threads.
 * Implements the DownloadFilePort interface for the hexagonal architecture.
 */
@UseCase
public class HttpFileDownloadAdapter implements DownloadFilePort, FileSystemFacade, UseLogging {

    private final HttpClient httpClient;

    public HttpFileDownloadAdapter() {
        // Configure HttpClient with virtual thread executor
        this.httpClient = HttpClient.newBuilder()
                .executor(Executors.newVirtualThreadPerTaskExecutor())
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    @Override
    public void downloadFile(String url, Path destination, Duration timeout) throws DownloadException {
        try {
            log().info("HttpFileDownloadAdapter :: Starting download from: {}", url);

            // Ensure parent directory exists
            ensureParentDirectoryExists(destination);


            // Create HTTP request with timeout
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(timeout)
                    .GET()
                    .build();

            // Download file
            HttpResponse<Path> response = httpClient.send(request, HttpResponse.BodyHandlers.ofFile(destination));

            // Check response status
            if (response.statusCode() != 200) {
                throw new DownloadException(
                        String.format("Download failed with HTTP status %d for URL: %s",
                                response.statusCode(), url)
                );
            }

            log().info("HttpFileDownloadAdapter :: Successfully downloaded to: {}", destination);
            FilterTournamentsService.tournamentDataUpdated = true;

        } catch (IOException e) {
            throw new DownloadException("I/O error during download from " + url, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DownloadException("Download interrupted from " + url, e);
        } catch (Exception e) {
            throw new DownloadException("Unexpected error downloading from " + url, e);
        }
    }

    /**
     * Ensures the parent directory of the destination file exists.
     */
    private void ensureParentDirectoryExists(Path destination) throws DownloadException {
        Path parentDir = destination.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            try {
                Files.createDirectories(parentDir);
                log().debug("HttpFileDownloadAdapter :: Created directory: {}", parentDir);
            } catch (IOException e) {
                throw new DownloadException("Failed to create directory: " + parentDir, e);
            }
        }
        assert parentDir != null;
        deleteAllFiles(parentDir.toString());
    }
}
