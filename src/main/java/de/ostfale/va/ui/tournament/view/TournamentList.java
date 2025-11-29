package de.ostfale.va.ui.tournament.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import de.ostfale.va.application.domain.model.Tournament;
import de.ostfale.va.application.domain.service.LoadTournamentsService;
import de.ostfale.va.application.port.in.LoadTournamentsFromCSV;
import de.ostfale.va.application.port.in.LoadTournamentsFromMemory;
import de.ostfale.va.application.port.in.TournamentsFilter;
import de.ostfale.va.common.UseLogging;

import java.util.List;

public class TournamentList extends VerticalLayout implements UseLogging {

    private final LoadTournamentsService loadTournamentsService = new LoadTournamentsService(new LoadTournamentsFromCSV().getAllTournaments());

    private final Grid<Tournament> grid;

    private List<Tournament> currentTournaments;

    public TournamentList() {
        log().debug("TournamentList :: Init tournament list view");
        this.grid = new Grid<>();
        configureGrid();
        add(grid);
    }

    private void configureGrid() {
        grid.addClassName("tournament-grid");
        grid.setHeightFull();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        addColumns();

        // Initialize with all tournaments
        currentTournaments = loadTournamentsService.loadTournaments();

        // Create lazy data provider for pagination
        DataProvider<Tournament, Void> dataProvider = DataProvider.fromCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    return currentTournaments.stream()
                            .skip(offset)
                            .limit(limit);
                },
                query -> currentTournaments.size()
        );

        grid.setDataProvider(dataProvider);
        grid.setPageSize(20);
    }

    private void addColumns() {
        addTextColumn(Tournament::startDate, "Datum", 0);
        addTextColumn(Tournament::closingDate, "Meldeschluss", 0);
        addTextColumn(Tournament::tournamentName, "Turniername", 1);
        addTextColumn(Tournament::location, "Ort", 1);
        addTextColumn(Tournament::tourCategory, "Kategorie", 0);
        addTextColumn(Tournament::organizer, "Organisation", 0);

        addLinkColumn(Tournament::webLinkUrl, VaadinIcon.LINK, "DBV Turnier");
        addLinkColumn(Tournament::pdfLinkUrl, VaadinIcon.FILE_TEXT_O, "Ausschreibung");
    }

    private void addTextColumn(com.vaadin.flow.function.ValueProvider<Tournament, ?> valueProvider, String header, int flexGrow) {
        grid.addColumn(valueProvider)
                .setHeader(header)
                .setAutoWidth(true)
                .setFlexGrow(flexGrow)
                .setResizable(true);
    }

    private void addLinkColumn(java.util.function.Function<Tournament, String> urlProvider, VaadinIcon icon, String header) {
        grid.addColumn(new ComponentRenderer<>(tournament ->
                        createLinkComponent(urlProvider.apply(tournament), icon.create())))
                .setHeader(header)
                .setAutoWidth(true)
                .setFlexGrow(0);
    }

    public void refresh(TournamentsFilter filter) {
        currentTournaments = loadTournamentsService.filter(filter);
        grid.getDataProvider().refreshAll();
    }

    public Grid<Tournament> getGrid() {
        return grid;
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
