package de.ostfale.va.adapter.out.filesystem;

import de.ostfale.va.application.domain.model.tournaments.Tournament;
import de.ostfale.va.application.domain.service.TournamentsCSVParserService;
import de.ostfale.va.application.port.out.LoadTournamentsPort;
import de.ostfale.va.common.FileSystemFacade;
import de.ostfale.va.common.UseLogging;

import java.util.List;

public class LoadTournamentsFileAdapter implements LoadTournamentsPort, UseLogging, FileSystemFacade {

    private final String applicationHomeDir = getApplicationSubDir(ApplicationDirectoryConfiguration.TOURNAMENT_DIR_NAME);
    private final TournamentsCSVParserService tournamentsCSVParserService = new TournamentsCSVParserService();

    @Override
    public List<Tournament> loadAll() {
        log().info("LoadTournamentsFileAdapter :: Loading tournaments from file system");
        var localTournamentFiles = readAllFiles(applicationHomeDir);
        log().info("LoadTournamentsFileAdapter :: Loading tournaments from CSV file found");
        return localTournamentFiles.stream()
                .flatMap(file -> tournamentsCSVParserService.parseTournamentCalendar(file).stream())
                .toList();
    }
}
