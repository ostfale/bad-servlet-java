package de.ostfale.va.application.port.in;

import de.ostfale.va.application.domain.model.AgeClass;
import de.ostfale.va.application.domain.model.AgeClassDisciplines;
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
                new Tournament(createDate(1, 10), createDate(1, 2), "C-RLT", "B-Level", "Kleinblittersdorf", "Saarland", "", "", createDisciplines()),
                new Tournament(createDate(2, 5), createDate(1, 15), "A-RLT", "A-Level", "Augsburg", "Bayern", "", "https://turniere.badminton.de/uploads/1306.pdf", createDisciplines()),
                new Tournament(createDate(2, 22), createDate(2, 3), "C-RLT THÜ", "C2-Level", "Gera", "Thüringen", "https://dbv.turnier.de/tournament/46A2907B-F220-4339-B396-F07EDFBAC794", "", createDisciplines())
        );
    }

    private List<AgeClassDisciplines> createDisciplines() {
        return List.of(
                createDiscipline(AgeClass.U15, true, true, true),
                createDiscipline(AgeClass.U17, true, false, false),
                createDiscipline(AgeClass.U19, false, true, true)
        );
    }

    private AgeClassDisciplines createDiscipline(AgeClass ageClass, boolean isSingle, boolean isDouble, boolean isMixed) {
        return new AgeClassDisciplines(ageClass, isSingle, isDouble, isMixed);
    }

    private LocalDate createDate(int month, int day) {
        return LocalDate.of(2026, month, day);
    }
}
