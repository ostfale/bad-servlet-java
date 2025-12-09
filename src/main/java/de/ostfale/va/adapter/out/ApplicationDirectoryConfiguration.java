package de.ostfale.va.adapter.out;

import de.ostfale.va.application.port.out.DirectoryConfiguration;
import de.ostfale.va.common.UseLogging;

import java.io.File;
import java.util.List;

public class ApplicationDirectoryConfiguration implements DirectoryConfiguration, UseLogging {

    private static final String SEP = File.separator;
    private static final String APP_NAME = ".bad-servlet";
    private static final String USER_HOME = "user.home";


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
                createDirectoryEntry("tournament", "tournament"),
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
