package de.ostfale.va.adapter.in.web;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import de.ostfale.va.common.UseLogging;

/**
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
@PWA(name = "Project Base for Vaadin", shortName = "Project Base")
@Theme("my-theme")
public class AppShell implements AppShellConfigurator, UseLogging {

    @Override
    public void configurePage(AppShellSettings settings) {
            AppShellConfigurator.super.configurePage(settings);
            log().info("AppShell :: configurePage");
    }
}
