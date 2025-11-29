package de.ostfale.va.application.port.in;

import de.ostfale.va.application.domain.model.AgeClass;
import de.ostfale.va.application.domain.model.AgeClassDisciplines;
import de.ostfale.va.application.domain.model.Tournament;
import de.ostfale.va.common.UseLogging;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoadTournamentsFromMemory implements UseLogging {

    private static final List<String> LOCATION_LIST = List.of(
            "Frauenreuth", "Hamburg", "Bremen", "Friedrichshafen", "Aalen", "Salzwedel", "Berlin", "Jena", "Freiburg",
            "Sandau", "Worms", "Neusäß", "Altenmarkt", "Völklingen", "Dossenheim", "Sandau (Elbe)", "Halle", "Stadtallendorf",
            "Marktheidenfeld", "Kleinblittersdorf", "Nienburg", "Sohland", "Berlin", "Landshut", "Neubrandenburg", "Hoyerwerda",
            "Alzenau (OT Michelbach)", "Mersebur", "Brietlingen", "Trier-Tarforst"
    );
    private static final List<String> ORGANIZER_LIST = List.of(
            "Hamburg", "Bremen", "Baden-Württemberg", "Sachsen", "Sachsen-Anhalt",
            "Berlin-Brandenburg", "Thüringen", "Rheinhessen-Pfalz", "Bayern", "DBV", "Hessen", "Niedersachsen", "Schleswig-Holstein");
    private final static List<String> KATEGORY_LIST = List.of(
            "D1-Level", "D2-Level", "E-Level", "C2-Level", "C1-Level", "B-Level", "A-Level", "BEC-U15"
    );
    private final List<Tournament> tournaments = new ArrayList<>();



    public List<Tournament> getAllTournaments() {
        if (tournaments.isEmpty()) {
            tournaments.addAll(createRandomList(100));
        }
        return tournaments;
    }

    public List<Tournament> filter(TournamentsFilter filter) {
        Objects.requireNonNull(filter, "'filter' must not be null");
        List<Tournament> tmp = new ArrayList<>();
        for (Tournament tournament : tournaments) {
            if (matches(filter, tournament)) {
                tmp.add(tournament);
            }
        }


        return tmp;
    }

    public int count(TournamentsFilter filter) {
        return 0;
    }

    private boolean matches(TournamentsFilter tournamentsFilter, Tournament tournament) {
        Objects.requireNonNull(tournamentsFilter, "'filter' must not be null");

        return tournamentsFilter.location()
                .map(searchLocation -> tournament.location().toLowerCase().contains(searchLocation.toLowerCase()))
                .orElse(false);
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

          /*  result.add(new Tournament(startDate, closingDate,
                    category + "-" + location, category, location, organizer, "", "", disciplines));*/
        }
        return result;
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
