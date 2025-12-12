package de.ostfale.va.application.port.out;

import java.util.List;

public interface DirectoryConfigurationPort {

    String basePath();

    List<DirectoryEntry> structure();

    interface DirectoryEntry {
        String path();
        boolean createIfMissing();
        boolean required();
        String name();
    }
}
