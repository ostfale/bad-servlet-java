package de.ostfale.va.application.domain.service;

import de.ostfale.va.application.domain.service.tournament.CalculateTournamentsStatistikService;
import de.ostfale.va.application.port.in.ScheduledDownloadUseCase;
import de.ostfale.va.application.port.out.DownloadFilePort;
import de.ostfale.va.application.port.out.LoadTournamentsPort;
import de.ostfale.va.application.port.out.TournamentFileDownloadConfigPort;
import de.ostfale.va.application.port.out.TournamentFileDownloadConfigPort.DownloadTask;
import de.ostfale.va.common.UseCase;
import de.ostfale.va.common.UseLogging;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Service implementing scheduled file downloads using virtual threads.
 * Downloads occur at a specified time each day with timeout and retry logic.
 */
@UseCase
public class ScheduledFileDownloadService implements ScheduledDownloadUseCase, UseLogging {

    private static final Duration DOWNLOAD_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration BATCH_TIMEOUT = Duration.ofMinutes(2);
    private static final Duration RETRY_DELAY = Duration.ofHours(1);

    private final DownloadFilePort downloadFilePort;
    private final TournamentFileDownloadConfigPort configurationPort;
    private final ScheduledExecutorService scheduler;
    private final AtomicBoolean running = new AtomicBoolean(false);


    private final LoadTournamentsPort loadTournamentsPort;
    private final CalculateTournamentsStatistikService calculateStatisticsService;
    private final TournamentStatisticsSignalService statisticsSignalService;

    public ScheduledFileDownloadService(DownloadFilePort downloadFilePort,
                                        TournamentFileDownloadConfigPort configurationPort,
                                        LoadTournamentsPort loadTournamentsPort,
                                        CalculateTournamentsStatistikService calculateStatisticsService,
                                        TournamentStatisticsSignalService statisticsSignalService) {
        this.downloadFilePort = downloadFilePort;
        this.configurationPort = configurationPort;
        this.loadTournamentsPort = loadTournamentsPort;
        this.calculateStatisticsService = calculateStatisticsService;
        this.statisticsSignalService = statisticsSignalService;

        // Use virtual thread executor for scheduling
        this.scheduler = Executors.newScheduledThreadPool(1, Thread.ofVirtual().factory());
    }

    @Override
    public void startScheduledDownloads() {
        if (running.get()) {
            log().warn("ScheduledFileDownloadService :: Scheduled downloads already running");
            return;
        }

        running.set(true);
        log().info("ScheduledFileDownloadService :: Starting scheduled downloads");

        LocalTime scheduledTime = configurationPort.getScheduledTime();
        long initialDelay = calculateInitialDelay(scheduledTime);

        scheduler.scheduleAtFixedRate(
                this::executeDownloads,
                initialDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS
        );

        log().info("ScheduledFileDownloadService :: Downloads scheduled for {} daily", scheduledTime);
    }

    @Override
    public void stopScheduledDownloads() {
        if (!running.get()) {
            log().warn("ScheduledFileDownloadService :: Scheduled downloads not running");
            return;
        }

        log().info("ScheduledFileDownloadService :: Stopping scheduled downloads");
        running.set(false);
        scheduler.shutdown();

        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    /**
     * Executes all configured downloads concurrently using virtual threads.
     * Each file is independent: failures do not cancel other downloads.
     */
    private void executeDownloads() {
        List<DownloadTask> tasks = configurationPort.getDownloadTasks();
        if (tasks.isEmpty()) {
            log().info("ScheduledFileDownloadService :: No download tasks configured");
            return;
        }

        log().info("ScheduledFileDownloadService :: Starting download of {} files", tasks.size());

        boolean allSuccessful;
        try {
            allSuccessful = downloadAllIndependently(tasks);
        } catch (Exception e) {
            log().error("ScheduledFileDownloadService :: Batch execution failed: {}", e.getMessage(), e);
            allSuccessful = false;
        }

        if (allSuccessful) {
            log().info("ScheduledFileDownloadService :: All downloads completed successfully");
            updateStatistics();
        } else {
            log().warn("ScheduledFileDownloadService :: Some downloads failed or timed out; scheduling retry");
            scheduleRetry();
        }
    }

    private void updateStatistics() {
        try {
            LocalDateTime downloadTime = LocalDateTime.now();
            String formattedDate = downloadTime.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

            // Load tournaments and calculate statistics
            var tournaments = loadTournamentsPort.loadAll();
            var statistics = calculateStatisticsService.loadTournamentsStatistik(tournaments, formattedDate);

            // Update the signal
            statisticsSignalService.updateStatistics(statistics);

            log().info("ScheduledFileDownloadService :: Statistics updated successfully");
        } catch (Exception e) {
            log().error("ScheduledFileDownloadService :: Failed to update statistics: {}", e.getMessage(), e);
        }
    }

    private boolean downloadAllIndependently(List<DownloadTask> tasks) throws InterruptedException {
        try (ExecutorService vt = Executors.newVirtualThreadPerTaskExecutor()) {

            var callables = tasks.stream()
                    .<Callable<Void>>map(task -> () -> {
                        downloadFilePort.downloadFile(task.url(), task.destination(), DOWNLOAD_TIMEOUT);
                        return null;
                    })
                    .toList();

            List<Future<Void>> futures = vt.invokeAll(callables, BATCH_TIMEOUT.toSeconds(), TimeUnit.SECONDS);

            boolean allSuccessful = true;

            for (int i = 0; i < futures.size(); i++) {
                var f = futures.get(i);
                var task = tasks.get(i);

                if (f.isCancelled()) {
                    allSuccessful = false;
                    // cancelled usually means batch timeout elapsed
                    log().warn("Download cancelled/timed out (batch): {} -> {}", task.url(), task.destination());
                    continue;
                }

                try {
                    f.get(); // will throw ExecutionException if that download failed
                    log().info("Completed: {}", task.url());
                } catch (ExecutionException e) {
                    allSuccessful = false;
                    log().error("Failed: {} -> {} ({})",
                            task.url(), task.destination(), e.getCause().getMessage(), e.getCause());
                }
            }
            return allSuccessful;
        }
    }

    private void scheduleRetry() {
        log().info("ScheduledFileDownloadService :: Scheduling retry in {} hour(s)", RETRY_DELAY.toHours());
        scheduler.schedule(
                this::executeDownloads,
                RETRY_DELAY.toSeconds(),
                TimeUnit.SECONDS
        );
    }

    private long calculateInitialDelay(LocalTime scheduledTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime scheduledDateTime = now.with(scheduledTime);

        if (scheduledDateTime.isBefore(now)) {
            scheduledDateTime = scheduledDateTime.plusDays(1);
        }

        long delaySeconds = ChronoUnit.SECONDS.between(now, scheduledDateTime);
        log().info("ScheduledFileDownloadService :: Initial delay: {} seconds ({} hours)",
                delaySeconds, delaySeconds / 3600);
        return delaySeconds;
    }
}
