package de.ostfale.va.adapter.out.filesystem;

import de.ostfale.va.application.port.out.DirectoryConfigurationPort;
import de.ostfale.va.common.FileSystemFacade;
import de.ostfale.va.common.UseLogging;

import java.util.List;

public class ApplicationDirectoryConfiguration implements DirectoryConfigurationPort, FileSystemFacade, UseLogging {

    public static final String APP_NAME = ".bad-servlet";
    public static final String TOURNAMENT_DIR_NAME = "tournament";

    @Override
    public String basePath() {
        var basePath = System.getProperty(USER_HOME) + SEP + APP_NAME;
        log().info("ApplicationDirectoryConfiguration :: Application base path: {}", basePath);
        return basePath;
    }

    @Override
    public List<DirectoryEntry> structure() {
        return List.of(
                createDirectoryEntry("config", "config"),
                createDirectoryEntry("db", "db"),
                createDirectoryEntry("data", "data"),
                createDirectoryEntry("dashboard", "data/dashboard"),
                createDirectoryEntry("favPlayer", "data/favPlayer"),
                createDirectoryEntry("favPlayerMatches", "data/favPlayer/matches"),
                createDirectoryEntry("favTournament", "data/favTournaments"),
                createDirectoryEntry("favTournamentFavorites", "data/favTournaments/favorites"),
                createDirectoryEntry("logs", "logs"),
                createDirectoryEntry(TOURNAMENT_DIR_NAME, "tournament"),
                createDirectoryEntry("ranking", "ranking")
        );
    }

    private DirectoryEntry createDirectoryEntry(String name, String path) {
        return new DirectoryEntry() {
            @Override
            public String path() {
                return path;
            }

            @Override
            public boolean createIfMissing() {
                return true;
            }

            @Override
            public boolean required() {
                return true;
            }

            @Override
            public String name() {
                return name;
            }
        };
    }
}
