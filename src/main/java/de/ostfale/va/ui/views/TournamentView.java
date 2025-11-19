package de.ostfale.va.ui.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.ostfale.va.application.domain.model.Tournament;
import de.ostfale.va.application.domain.service.LoadTournamentsService;
import de.ostfale.va.application.port.in.LoadTournamentsFromMemory;
import de.ostfale.va.common.UseLogging;
import de.ostfale.va.ui.MainLayout;


@Route(value = TournamentView.PATH, layout = MainLayout.class)
public class TournamentView extends VerticalLayout implements UseLogging {

    public static final String PATH = "tournament";
    private final Grid<Tournament> grid = new Grid<>(Tournament.class, false);

    private final LoadTournamentsService loadTournamentsService = new LoadTournamentsService(new LoadTournamentsFromMemory());


    public TournamentView() {
        log().info("TournamentView :: constructor");
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        grid.addColumn(Tournament::name).setHeader("Name");
        grid.addColumn(Tournament::location).setHeader("Location");
        grid.addColumn(Tournament::date).setHeader("Date");

        grid.setItems(loadTournamentsService.loadTournaments());
        grid.setSizeFull();

        add(new H2("Upcoming Tournaments"), grid);
    }

}
