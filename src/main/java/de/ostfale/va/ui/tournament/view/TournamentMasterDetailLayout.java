package de.ostfale.va.ui.tournament.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.masterdetaillayout.MasterDetailLayout;
import com.vaadin.flow.router.Route;
import de.ostfale.va.application.domain.model.Tournament;
import de.ostfale.va.application.domain.service.LoadTournamentsService;
import de.ostfale.va.application.port.in.LoadTournamentsFromMemory;
import de.ostfale.va.common.UseLogging;
import de.ostfale.va.ui.app.view.MainView;

@Route(value = TournamentMasterDetailLayout.PATH, layout = MainView.class)
public class TournamentMasterDetailLayout extends Div implements UseLogging {

    public static final String PATH = "tournament-master-detail-layout";

    private final LoadTournamentsService loadTournamentsService = new LoadTournamentsService(new LoadTournamentsFromMemory());

    public TournamentMasterDetailLayout() {
        MasterDetailLayout layout = new MasterDetailLayout();
        layout.setOverlayMode(MasterDetailLayout.OverlayMode.DRAWER);
        layout.setSizeFull();

        TournamentList tournamentList = new TournamentList(loadTournamentsService.loadTournaments());
        layout.setMaster(tournamentList);
        tournamentList.setSizeFull();

        TournamentDetails tournamentDetails = new TournamentDetails();
        tournamentDetails.setSizeFull();

        tournamentList.getGrid().asSingleSelect().addValueChangeListener(event -> {
            Tournament selectedTournament = event.getValue();

            if (selectedTournament != null) {
                tournamentDetails.setTournament(selectedTournament);
                layout.setDetail(tournamentDetails);
            } else {
                layout.setDetail(null);
            }
        });

        tournamentDetails.addCloseListener(event -> tournamentList.getGrid().deselectAll());
        add(layout);
        setHeightFull();
    }
}
