package de.ostfale.va.ui.tournament.controls;

import de.ostfale.va.application.domain.model.Tournament;
import de.ostfale.va.application.port.in.LoadTournamentsFromCSV;
import de.ostfale.va.application.port.in.TournamentsFilter;
import de.ostfale.va.common.UseLogging;

import java.util.List;
import java.util.stream.Stream;

public class DataSource implements UseLogging {

    private final List<Tournament> tournaments = new LoadTournamentsFromCSV().getAllTournaments();

    public Stream<Tournament> fetch(TournamentsFilter filter, int offset, int limit) {
        return tournaments.stream()
                .filter(tournament -> matchesFilter(tournament, filter))
                .skip(offset)
                .limit(limit);
    }

    public int count(TournamentsFilter filter) {
        int result = (int) tournaments.stream()
                .filter(tournament -> matchesFilter(tournament, filter))
                .count();
        log().debug("DataSource :: Found {} tournaments", result);
        return result;
    }

    private boolean matchesFilter(Tournament tournament, TournamentsFilter filter) {
        if (filter == null) {
            return true;
        }

        return matches(filter.name().orElse(null), tournament.tournamentName())
                && matches(filter.location().orElse(null), tournament.location());
    }

    private boolean matches(String filterValue, String actualValue) {
        if (filterValue == null || filterValue.isEmpty()) {
            return true;
        }
        return actualValue != null && actualValue.toLowerCase().contains(filterValue.toLowerCase());
    }
}
