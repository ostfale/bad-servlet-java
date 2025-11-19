package de.ostfale.va.application.port.in;

import de.ostfale.va.application.domain.model.Tournament;
import de.ostfale.va.common.UseLogging;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoadTournamentsFromMemory implements LoadTournamentsUseCase, UseLogging {

    private final List<Tournament> tournaments = new ArrayList<>();

    @Override
    public List<Tournament> loadTournaments() {
        if (tournaments.isEmpty()) {
            tournaments.addAll(initTournaments());
        }
        return tournaments;
    }

    private List<Tournament> initTournaments() {
        log().info("LoadTournamentsFromMemory :: initTournaments");
        return List.of(
                createTournament("C-RL", "Hamburg", LocalDate.of(2025, 11, 25)),
                createTournament("B-RL", "Hannover", LocalDate.of(2025, 12, 7)),
                createTournament("A-RL", "Bremen", LocalDate.of(2025, 12, 17))
        );
    }

    private Tournament createTournament(String name, String location, LocalDate date) {
        return new Tournament(name, location, date);
    }
}
