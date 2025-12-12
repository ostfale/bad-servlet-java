package de.ostfale.va.application.port.out;

import java.nio.file.Path;
import java.time.Duration;

/**
 * Port for downloading files from external sources.
 * Implementations should handle HTTP/HTTPS connections and file I/O.
 */
public interface DownloadFilePort {

    /**
     * Downloads a file from the specified URL to the destination path.
     *
     * @param url         The URL to download from
     * @param destination The local file path where the downloaded file will be saved
     * @param timeout     Maximum time to wait for the download to complete
     * @throws DownloadException if the download fails for any reason
     */
    void downloadFile(String url, Path destination, Duration timeout) throws DownloadException;

    /**
     * Exception thrown when a download operation fails.
     */
    class DownloadException extends Exception {
        public DownloadException(String message) {
            super(message);
        }

        public DownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
