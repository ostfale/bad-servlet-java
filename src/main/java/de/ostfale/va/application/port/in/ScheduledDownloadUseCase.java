package de.ostfale.va.application.port.in;

/**
 * Use case for managing scheduled file downloads.
 */
public interface ScheduledDownloadUseCase {

    /**
     * Starts the scheduled download service.
     * Downloads will occur at the configured time each day.
     */
    void startScheduledDownloads();

    /**
     * Stops the scheduled download service and cancels any pending downloads.
     */
    void stopScheduledDownloads();

    /**
     * Checks if the scheduled download service is currently running.
     *
     * @return true if running, false otherwise
     */
    boolean isRunning();
}
