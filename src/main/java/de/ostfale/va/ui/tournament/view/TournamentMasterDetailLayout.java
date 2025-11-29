package de.ostfale.va.ui.tournament.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.masterdetaillayout.MasterDetailLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.ostfale.va.application.port.in.TournamentsFilter;
import de.ostfale.va.common.UseLogging;
import de.ostfale.va.ui.app.view.MainView;

@Route(value = TournamentMasterDetailLayout.PATH, layout = MainView.class)
public class TournamentMasterDetailLayout extends Div implements UseLogging {

    public static final String PATH = "tournament-master-detail-layout";

    public TournamentMasterDetailLayout() {
        log().debug("TournamentMasterDetailLayout :: constructor");
        setSizeFull();

        // Create filter panel
        TournamentFilterPanel filterPanel = new TournamentFilterPanel();

        // Create master-detail layout
        MasterDetailLayout layout = new MasterDetailLayout();
        layout.setOverlayMode(MasterDetailLayout.OverlayMode.DRAWER);
        layout.setSizeFull();

        TournamentList tournamentList = new TournamentList();
        tournamentList.setSizeFull();
        layout.setMaster(tournamentList);

        TournamentDetails tournamentDetails = new TournamentDetails();
        tournamentDetails.setSizeFull();

        tournamentList.getGrid().asSingleSelect().addValueChangeListener(event -> {
            var selectedTournament = event.getValue();
            if (selectedTournament != null) {
                tournamentDetails.setTournament(selectedTournament);
                layout.setDetail(tournamentDetails);
            } else {
                layout.setDetail(null);
            }
        });

        tournamentDetails.addCloseListener(event -> tournamentList.getGrid().deselectAll());

        // Add filter listener
        filterPanel.addFilterChangeListener(event -> {
            TournamentsFilter filter = event.getFilter();
            tournamentList.refresh(filter);
        });

        // Use VerticalLayout to stack filter above master-detail
        VerticalLayout mainLayout = new VerticalLayout(filterPanel, layout);
        mainLayout.setSizeFull();
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setFlexGrow(0, filterPanel);  // Filter panel takes only needed space
        mainLayout.setFlexGrow(1, layout);       // Master-detail takes remaining space

        add(mainLayout);
    }
}
