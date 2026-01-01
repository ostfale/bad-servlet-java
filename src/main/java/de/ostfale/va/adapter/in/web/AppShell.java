package de.ostfale.va.adapter.in.web;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.PWA;
import de.ostfale.va.common.UseLogging;

/**
 * Use the @PWA annotation to make the application installable on phones, tablets
 * and some desktop browsers.
 */
@PWA(name = "Project Base for Vaadin", shortName = "Project Base")
@StyleSheet("./themes/my-theme/styles.css")
public class AppShell implements AppShellConfigurator, UseLogging {

    private static final String LOG_MSG_CONFIGURE = "AppShell :: configurePage";

    @Override
    public void configurePage(AppShellSettings settings) {
        log().info(LOG_MSG_CONFIGURE);
    }
}
