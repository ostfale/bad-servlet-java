package de.ostfale.va.adapter.in.web.dashboard;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.ostfale.va.adapter.in.web.app.MainView;
import de.ostfale.va.adapter.in.web.dashboard.components.DownloadInfoCard;
import de.ostfale.va.application.domain.service.TournamentStatisticsSignalService;
import de.ostfale.va.common.UseLogging;

@Route(value = "", layout = MainView.class)
public class OverviewView extends VerticalLayout implements UseLogging {

    public static final String PATH = "";

    public OverviewView() {
        log().info("OverviewView :: constructor");
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        // Add the download info card
        add(new DownloadInfoCard(TournamentStatisticsSignalService.getInstance()));
    }
}
