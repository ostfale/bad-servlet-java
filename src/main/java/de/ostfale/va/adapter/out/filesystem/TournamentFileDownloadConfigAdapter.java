package de.ostfale.va.adapter.out.filesystem;

import de.ostfale.va.application.port.out.TournamentFileDownloadConfigPort;
import de.ostfale.va.common.FileSystemFacade;
import de.ostfale.va.common.TimeHandlerFacade;
import de.ostfale.va.common.UseLogging;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TournamentFileDownloadConfigAdapter implements TournamentFileDownloadConfigPort, FileSystemFacade, TimeHandlerFacade, UseLogging {

    private static final String FILE_NAME = "Tournament";
    private static final String FILE_SUFFIX = ".csv";
    private static final String DATE_SEPARATOR = "_";

    @Override
    public List<DownloadTask> getDownloadTasks() {
        var currentYear = getActualCalendarYear();
        var nextYear = currentYear++;

        var fileNameThisYear = prepareDownloadFileName(String.valueOf(currentYear));
        var fileNameNextYear = prepareDownloadFileName(String.valueOf(nextYear));

        var destinationPath = prepareDownloadTargetPath(ApplicationDirectoryConfiguration.TOURNAMENT_DIR_NAME);

        var downloadUrlThisYear = prepareDownloadUrl(String.valueOf(currentYear));
        var downloadUrlNextYear = prepareDownloadUrl(String.valueOf(nextYear));

        var downloadTaskThisYear = createDownloadTask(downloadUrlThisYear, destinationPath + fileNameThisYear);
        var downloadTaskNextYear = createDownloadTask(downloadUrlNextYear, destinationPath + fileNameNextYear);

        return List.of(downloadTaskThisYear, downloadTaskNextYear);
    }

    @Override
    public LocalTime getScheduledTime() {
        return LocalTime.of(18, 13);
    }

    @Override
    public String prepareDownloadFileName(String year) {
        String tFileName = FILE_NAME + DATE_SEPARATOR + year + DATE_SEPARATOR + LocalDate.now() + FILE_SUFFIX;
        log().debug("TournamentFileDownloadConfigAdapter :: prepareDownloadFileName: {}", tFileName);
        return tFileName;
    }

    @Override
    public String prepareDownloadTargetPath(String appDirName) {
        var appDir = getApplicationHomeDir();
        var tourDir = appDir + SEP + appDirName + SEP;
        log().debug("TournamentFileDownloadConfigAdapter :: prepareDownloadTargetPath: {}", tourDir);
        return tourDir;
    }

    @Override
    public String prepareDownloadUrl(String year) {
        return String.format("%s%s%s", TOURNAMENT_DOWNLOAD_URL_PREFIX, year, TOURNAMENT_DOWNLOAD_SEARCH_PARAM);
    }

    private DownloadTask createDownloadTask(String url, String destination) {
        log().debug("TournamentFileDownloadConfigAdapter :: createDownloadTask: {} -> {}", url, destination);
        return new DownloadTask(url, Path.of(destination));
    }
}
