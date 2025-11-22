package de.ostfale.va.ui.tournament.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import de.ostfale.va.application.domain.model.Tournament;
import de.ostfale.va.application.domain.service.LoadTournamentsService;
import de.ostfale.va.application.port.in.LoadTournamentsFromMemory;
import de.ostfale.va.common.UseLogging;
import de.ostfale.va.ui.app.view.MainView;

@Route(value = TournamentView.PATH, layout = MainView.class)
public class TournamentView extends VerticalLayout implements UseLogging {

    public static final String PATH = "tournament";
    private final Grid<Tournament> grid = new Grid<>(Tournament.class, false);
    private final LoadTournamentsService loadTournamentsService = new LoadTournamentsService(new LoadTournamentsFromMemory());

    public TournamentView() {
        log().info("TournamentView :: constructor");
        configureLayout();
        configureGrid();
        updateList();
        add(new H2("Upcoming Tournaments"), grid);
    }

    private void configureLayout() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);
    }

    private void configureGrid() {
        grid.addColumn(Tournament::startDate).setHeader("Datum");
        grid.addColumn(Tournament::closedDate).setHeader("Meldeschluss");
        grid.addColumn(Tournament::tournamentName).setHeader("Turniername");
        grid.addColumn(Tournament::location).setHeader("Ort");
        grid.addColumn(Tournament::categoryName).setHeader("Kategorie");
        grid.addColumn(Tournament::organizer).setHeader("Organisation");

        grid.addColumn(new ComponentRenderer<>(tournament ->
                createLinkComponent(tournament.webLinkUrl(), VaadinIcon.LINK.create())
        )).setHeader("DBV Turnier");

        grid.addColumn(new ComponentRenderer<>(tournament ->
                createLinkComponent(tournament.pdfLinkUrl(), VaadinIcon.FILE_TEXT_O.create())
        )).setHeader("Ausschreibung");

        grid.setSizeFull();
    }

    private void updateList() {
        grid.setItems(loadTournamentsService.loadTournaments());
    }

    private Component createLinkComponent(String url, Component icon) {
        if (url != null && !url.isEmpty()) {
            Anchor link = new Anchor(url, icon);
            link.setTarget("_blank");
            return link;
        }
        return new Span("-");
    }
}
