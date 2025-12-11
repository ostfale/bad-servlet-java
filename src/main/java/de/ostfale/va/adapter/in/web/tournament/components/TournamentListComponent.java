package de.ostfale.va.adapter.in.web.tournament.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import de.ostfale.va.application.domain.model.tournaments.Tournament;
import de.ostfale.va.application.port.in.tournaments.TournamentsFilter;
import de.ostfale.va.common.UseLogging;

public class TournamentListComponent extends VerticalLayout implements UseLogging {

    private final Grid<Tournament> grid;
    private final PaginationComponent paginationComponent;

    private final DataProvider<Tournament, TournamentsFilter> dataProvider;

    public TournamentListComponent(DataProvider<Tournament, TournamentsFilter> pagingDataProvider, PaginationComponent paginationComponent) {
        log().debug("TournamentList :: Init tournament list view");
        this.dataProvider = pagingDataProvider;
        this.paginationComponent = paginationComponent;
        this.grid = new Grid<>();
        configureGrid();
        add(grid, paginationComponent);
        setFlexGrow(1, grid);

        this.paginationComponent.setPageChangedListener(() -> {
            grid.setPageSize(paginationComponent.getPageSize());
            grid.getDataProvider().refreshAll();
        });
    }

    private void configureGrid() {
        grid.addClassName("tournament-grid");
        grid.setHeightFull();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        addColumns();

        var dataProvider = this.dataProvider.withConfigurableFilter();
        grid.setDataProvider(dataProvider);
        grid.setPageSize(paginationComponent.getPageSize());
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
        @SuppressWarnings("unchecked")
        var dataProvider = (ConfigurableFilterDataProvider<Tournament, Void, TournamentsFilter>) grid.getDataProvider();
        dataProvider.setFilter(filter);
        paginationComponent.reset();
    }

    public Grid<Tournament> getGrid() {
        return grid;
    }

    private Component createLinkComponent(String url, Component icon) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.setWidthFull();

        if (url != null && !url.isEmpty()) {
            icon.addClassName("tournament-link-icon");
            Anchor link = new Anchor(url, icon);
            link.setTarget("_blank");
            layout.add(link);
        } else {
            layout.add(new Span("-"));
        }
        return layout;
    }
}
