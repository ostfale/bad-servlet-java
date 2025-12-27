package de.ostfale.va.application.domain.service;

import de.ostfale.va.adapter.out.filesystem.ApplicationDirectoryConfiguration;
import de.ostfale.va.application.domain.model.tournaments.TournamentStatistics;
import de.ostfale.va.common.FileSystemFacade;
import de.ostfale.va.common.UseLogging;

import java.io.File;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LastDownloadService implements FileSystemFacade,UseLogging {

    private static final Pattern FILENAME_PATTERN = Pattern.compile("Tournament_\\d{4}_(\\d{4}-\\d{2}-\\d{2})\\.csv");

/*    private final CalculateTournamentsStatistikService calculateTournamentsStatistikService;

    public LastDownloadService(CalculateTournamentsStatistikService calculateTournamentsStatistikService) {
        this.calculateTournamentsStatistikService = calculateTournamentsStatistikService;
    }*/

    public Optional<LocalDate> getLastDownloadDate() {
        String tournamentDir = getApplicationSubDir(ApplicationDirectoryConfiguration.TOURNAMENT_DIR_NAME);
        var files = readAllFiles(tournamentDir);

        return files.stream()
                .map(File::getName)
                .map(FILENAME_PATTERN::matcher)
                .filter(Matcher::matches)
                .map(matcher -> matcher.group(1))
                .map(LocalDate::parse)
                .max(Comparator.naturalOrder());
    }

    public TournamentStatistics getLastDownloadStatistik() {
        var tournamentDir = getApplicationSubDir(ApplicationDirectoryConfiguration.TOURNAMENT_DIR_NAME);
        var lastDownloadDate = readDownloadDateFromFileName(tournamentDir);

        return null;
    }

    private String readDownloadDateFromFileName(String tournamentDirectory) {
        log().debug("LastDownloadService :: read tournaments from : {}", tournamentDirectory);
        var files = readAllFiles(tournamentDirectory);
        var result = files.stream()
                .map(File::getName)
                .map(FILENAME_PATTERN::matcher)
                .filter(Matcher::matches)
                .map(matcher -> matcher.group(1))
                .max(Comparator.naturalOrder());
        log().debug("LastDownloadService :: last tournament date: {}", result);
        return result.orElse("-");
    }
}
