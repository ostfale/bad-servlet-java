package de.ostfale.va.common;

import de.ostfale.va.adapter.out.filesystem.HttpFileDownloadAdapter;
import de.ostfale.va.adapter.out.filesystem.TournamentFileDownloadConfigAdapter;
import de.ostfale.va.application.domain.service.ApplyApplicationStructureService;
import de.ostfale.va.application.domain.service.ScheduledFileDownloadService;
import de.ostfale.va.application.port.in.CreateDirectoryStructureUseCase;
import de.ostfale.va.application.port.in.ScheduledDownloadUseCase;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.List;

@WebListener
public class ApplicationContextListener implements ServletContextListener, UseLogging {

    private final CreateDirectoryStructureUseCase createDirectoryStructureUseCase = new ApplyApplicationStructureService();
    private ScheduledDownloadUseCase scheduledDownloadUseCase;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log().info("ApplicationContextListener :: contextInitialized");
        List<String> errors = createDirectoryStructureUseCase.validateAndCreateDirectoryStructure();

        if (!errors.isEmpty()) {
            log().error("Directory structure validation failed with {} errors:", errors.size());
            errors.forEach(log()::error);

            // Decide whether to continue or fail fast
            boolean hasCriticalErrors = errors.stream()
                    .anyMatch(error -> error.contains("Required directory"));

            if (hasCriticalErrors) {
                throw new RuntimeException("Critical directory structure validation failed");
            }
        } else {
            log().info("ApplicationContextListener :: Directory structure validation completed successfully");
        }

        // Initialize scheduled downloads
        scheduledDownloadUseCase = new ScheduledFileDownloadService(new HttpFileDownloadAdapter(), new TournamentFileDownloadConfigAdapter());
        scheduledDownloadUseCase.startScheduledDownloads();
        log().info("ApplicationContextListener :: Scheduled downloads initialization skipped (not configured)");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log().info("ApplicationContextListener :: contextDestroyed");

        // Stop scheduled downloads gracefully
        if (scheduledDownloadUseCase != null && scheduledDownloadUseCase.isRunning()) {
            log().info("ApplicationContextListener :: Stopping scheduled downloads");
            scheduledDownloadUseCase.stopScheduledDownloads();
        }
    }
}
