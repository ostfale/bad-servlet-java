package de.ostfale.va.common;

import com.github.javaparser.utils.Log;
import de.ostfale.va.application.domain.service.ApplyApplicationStructureService;
import de.ostfale.va.application.port.in.DirectoryStructureUseCase;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.List;

@WebListener
public class ApplicationContextListener implements ServletContextListener, UseLogging {

    private final DirectoryStructureUseCase directoryStructureUseCase = new ApplyApplicationStructureService();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log().info("ApplicationContextListener :: contextInitialized");
        List<String> errors = directoryStructureUseCase.validateAndCreateDirectoryStructure();

        if (!errors.isEmpty()) {
            log().error("Directory structure validation failed with {} errors:", errors.size());
            errors.forEach(Log::error);

            // Decide whether to continue or fail fast
            boolean hasCriticalErrors = errors.stream()
                    .anyMatch(error -> error.contains("Required directory"));

            if (hasCriticalErrors) {
                throw new RuntimeException("Critical directory structure validation failed");
            }
        } else {
            log().info("ApplicationContextListener :: Directory structure validation completed successfully");
        }
    }
}
