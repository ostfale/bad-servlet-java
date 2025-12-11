package de.ostfale.va.common;

import de.ostfale.va.adapter.out.filesystem.ApplicationDirectoryConfiguration;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface FileSystemFacade {

    String SEP = File.separator;
    String USER_HOME = "user.home";

    default String getHomeDir() {
        return System.getProperty(USER_HOME);
    }

    default String getApplicationHomeDir() {
        return getHomeDir() + SEP + ApplicationDirectoryConfiguration.APP_NAME;
    }

    default String getApplicationSubDir(String subDirName) {
        return getApplicationHomeDir() + SEP + subDirName;
    }

    default List<File> readAllFiles(String dirPath) {
        return Stream.ofNullable(new File(dirPath).listFiles()).flatMap(Stream::of).filter(File::isFile).collect(Collectors.toList());
    }

}
