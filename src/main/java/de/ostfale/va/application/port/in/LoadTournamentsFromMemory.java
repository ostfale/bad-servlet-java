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

    private static final List<String> LOCATION_LIST = List.of(
          "Frauenreuth", "Hamburg", "Bremen", "Friedrichshafen", "Aalen", "Salzwedel", "Berlin", "Jena" , "Freiburg",
            "Sandau", "Worms", "Neusäß", "Altenmarkt", "Völklingen", "Dossenheim", "Sandau (Elbe)", "Halle", "Stadtallendorf",
            "Marktheidenfeld", "Kleinblittersdorf", "Nienburg", "Sohland", "Berlin", "Landshut", "Neubrandenburg", "Hoyerwerda",
            "Alzenau (OT Michelbach)", "Mersebur", "Brietlingen", "Trier-Tarforst"
    );

    private static final List<String> ORGANIZER_LIST = List.of(
            "Hamburg", "Bremen", "Baden-Württemberg", "Sachsen","Sachsen-Anhalt",
            "Berlin-Brandenburg","Thüringen", "Rheinhessen-Pfalz", "Bayern", "DBV", "Hessen", "Niedersachsen", "Schleswig-Holstein");

    private final static List<String> KATEGORY_LIST = List.of(
            "D1-Level","D2-Level","E-Level","C2-Level","C1-Level","B-Level", "A-Level","BEC-U15"
    );
    
    
    
    @Override
    public List<Tournament> loadTournaments() {
        if (tournaments.isEmpty()) {
            tournaments.addAll(createRandomList(100));
        }
        return tournaments;
    }

    private List<Tournament> createRandomList(int nofEntries) {
        List<Tournament> result = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.plusWeeks(2);
        LocalDate closingDate = startDate.minusWeeks(1);

        for (long i = 0; i < nofEntries; i++) {
            String location = LOCATION_LIST.get((int) (Math.random() * LOCATION_LIST.size()));
            String organizer = ORGANIZER_LIST.get((int) (Math.random() * ORGANIZER_LIST.size()));
            String category = KATEGORY_LIST.get((int) (Math.random() * KATEGORY_LIST.size()));

            List<AgeClassDisciplines> disciplines = List.of(
                    createDiscipline(AgeClass.values()[(int) (Math.random() * AgeClass.values().length)],
                            Math.random() < 0.5,
                            Math.random() < 0.5,
                            Math.random() < 0.5)
            );

            result.add(new Tournament(startDate, closingDate,
                    category + "-" + location, category, location, organizer, "", "", disciplines));
        }
        return result;
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
