package de.ostfale.va.common;

import java.io.File;

public interface FileSystemFacade{

    String SEP = File.separator;;
    String USER_HOME = "user.home";


    default String getHomeDir() {
        return System.getProperty(USER_HOME);
    }

    default String getApplicationHomeDir(String APP_DIR_NAME) {
        return getHomeDir() + SEP + APP_DIR_NAME;
    }

}
