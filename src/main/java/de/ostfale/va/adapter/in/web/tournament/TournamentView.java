package de.ostfale.va.adapter.in.web.tournament;

import com.vaadin.flow.component.masterdetaillayout.MasterDetailLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.Route;
import de.ostfale.va.adapter.in.web.app.MainView;
import de.ostfale.va.adapter.in.web.tournament.components.PaginationComponent;
import de.ostfale.va.adapter.in.web.tournament.components.TournamentDetailsComponent;
import de.ostfale.va.adapter.in.web.tournament.components.TournamentFilterComponent;
import de.ostfale.va.adapter.in.web.tournament.components.TournamentListComponent;
import de.ostfale.va.application.domain.model.tournaments.Tournament;
import de.ostfale.va.application.port.in.FilterTournamentsUseCase;
import de.ostfale.va.application.domain.service.tournament.TournamentsFilter;
import de.ostfale.va.common.ServiceRegistry;
import de.ostfale.va.common.UseLogging;

import java.util.stream.Stream;

@Route(value = TournamentView.PATH, layout = MainView.class)
public class TournamentView extends VerticalLayout implements UseLogging {

    public static final String PATH = "tournament-view";

    private final PaginationComponent paginationComponent = new PaginationComponent();

    // Configured via Registry, typed as the Interface
    private final FilterTournamentsUseCase filterTournamentsUseCase;

    private final DataProvider<Tournament, TournamentsFilter> pagingDataProvider;

    public TournamentView() {
        // Manual lookup from the Registry
        // This keeps the View ignorant of "FilterTournamentsService" or "CSVParser"
        this.filterTournamentsUseCase = ServiceRegistry.getInstance().getFilterTournamentsUseCase();
        this.pagingDataProvider = DataProvider.fromFilteringCallbacks(this::fetchTournaments, this::countTournaments);

        log().info("TournamentView :: constructor");
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        initLayout();
    }

    private Stream<Tournament> fetchTournaments(Query<Tournament, TournamentsFilter> query) {
        // Use the offset and limit provided by the Grid's query (via PaginationComponent)
        var offset = paginationComponent.calculateOffset();
        var limit = paginationComponent.getPageSize();
        var filter = query.getFilter().orElse(null);

        log().debug("TournamentView :: pagingDataProvider :: limit: {}, offset: {}", limit, offset);
        // Pass query parameters directly to the backend
        return filterTournamentsUseCase.fetch(filter, offset, limit);
    }

    private int countTournaments(Query<Tournament, TournamentsFilter> query) {
        var filter = query.getFilter().orElse(null);
        // Get TOTAL count for the pagination component to update UI buttons
        int totalItems = filterTournamentsUseCase.count(filter);
        paginationComponent.setTotalItemCount(totalItems);

        // Return the count of items for the CURRENT PAGE only to the Grid
        // This tricks the Grid into displaying only the current page's worth of data
        var offset = paginationComponent.calculateOffset();
        var limit = paginationComponent.getPageSize();
        return Math.max(0, Math.min(limit, totalItems - offset));
    }

    private void initLayout() {
        log().debug("TournamentView :: initLayout");
        var tournamentListComponent = createTournamentListComponent(pagingDataProvider, paginationComponent);
        var tournamentFilterComponent = createFilterComponent(tournamentListComponent);
        var tournamentMasterDetailComponent = createTournamentMasterDetailComponent(tournamentListComponent);


        // Use VerticalLayout to stack filter above master-detail
        VerticalLayout mainLayout = new VerticalLayout(tournamentFilterComponent, tournamentMasterDetailComponent);
        mainLayout.setSizeFull();
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setFlexGrow(0, tournamentFilterComponent);       // Filter panel takes only needed space
        mainLayout.setFlexGrow(1, tournamentMasterDetailComponent); // Master-detail takes remaining space
        add(mainLayout);
    }


    private TournamentFilterComponent createFilterComponent(TournamentListComponent tListComponent) {
        log().debug("TournamentView :: createFilterPanel");
        var filterComponent = new TournamentFilterComponent();

        filterComponent.addFilterChangeListener(event -> {
            TournamentsFilter filter = event.getFilter();
            tListComponent.refresh(filter);
        });

        return filterComponent;
    }


    private TournamentListComponent createTournamentListComponent(DataProvider<Tournament, TournamentsFilter> pagingDataProvider, PaginationComponent paginationComponent) {
        log().debug("TournamentView :: createTournamentListComponent");
        var component = new TournamentListComponent(pagingDataProvider, paginationComponent);
        component.setSizeFull();
        return component;
    }

    private TournamentDetailsComponent createTournamentDetailsComponent() {
        log().debug("TournamentView :: createTournamentDetailsComponent");
        return new TournamentDetailsComponent();
    }

    private MasterDetailLayout createTournamentMasterDetailComponent(TournamentListComponent tournamentListComponent) {
        log().debug("TournamentView :: createTournamentMasterDetailComponent");

        var tournamentDetailsComponent = createTournamentDetailsComponent();

        MasterDetailLayout masterDetailLayout = new MasterDetailLayout();
        masterDetailLayout.setMaster(tournamentListComponent);
        masterDetailLayout.setOverlayMode(MasterDetailLayout.OverlayMode.DRAWER);
        masterDetailLayout.setSizeFull();

        tournamentListComponent.getGrid().asSingleSelect().addValueChangeListener(event -> {
            var selectedTournament = event.getValue();
            if (selectedTournament != null) {
                tournamentDetailsComponent.setTournament(selectedTournament);
                masterDetailLayout.setDetail(tournamentDetailsComponent);
            } else {
                masterDetailLayout.setDetail(null);
            }
        });

        tournamentDetailsComponent.addCloseListener(event -> tournamentListComponent.getGrid().deselectAll());
        return masterDetailLayout;
    }
}
