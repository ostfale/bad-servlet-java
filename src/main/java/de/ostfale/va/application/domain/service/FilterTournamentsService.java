package de.ostfale.va.application.domain.service;

import de.ostfale.va.application.domain.model.tournaments.AgeClass;
import de.ostfale.va.application.domain.model.tournaments.TourCategory;
import de.ostfale.va.application.domain.model.tournaments.Tournament;
import de.ostfale.va.application.domain.service.tournament.TournamentsFilter;
import de.ostfale.va.application.port.in.FilterTournamentsUseCase;
import de.ostfale.va.application.port.out.LoadTournamentsPort;
import de.ostfale.va.common.TimeHandlerFacade;
import de.ostfale.va.common.UseCase;
import de.ostfale.va.common.UseLogging;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@UseCase
public class FilterTournamentsService implements FilterTournamentsUseCase, TimeHandlerFacade, UseLogging {

    public static volatile boolean tournamentDataUpdated = false;

    private final LoadTournamentsPort tournamentsPort;
    private List<Tournament> tournaments;

    public FilterTournamentsService(LoadTournamentsPort tournaments) {
        this.tournamentsPort = tournaments;
        this.tournaments = tournaments.loadAll();
    }

    @Override
    public Stream<Tournament> fetch(TournamentsFilter filter, int offset, int limit) {
        if (tournamentDataUpdated) {
            reloadTournaments();
        }

        return tournaments.stream()
                .filter(tournament -> matchesFilter(tournament, filter))
                .sorted((t1, t2) -> {
                    LocalDate date1 = parseDateToTournamentFormat(t1.startDate());
                    LocalDate date2 = parseDateToTournamentFormat(t2.startDate());
                    return date1.compareTo(date2);
                })
                .skip(offset)
                .limit(limit);
    }

    @Override
    public int count(TournamentsFilter filter) {
        int result = (int) tournaments.stream()
                .filter(tournament -> matchesFilter(tournament, filter))
                .count();
        log().debug("DataSource :: Found {} tournaments", result);
        return result;
    }

    private void reloadTournaments() {
        log().debug("DataSource :: Reloading tournaments");
        tournaments.clear();
        tournaments = tournamentsPort.loadAll();
        tournamentDataUpdated = false;
    }

    private boolean matchesFilter(Tournament tournament, TournamentsFilter filter) {
        if (filter == null) {
            return true;
        }

        if (filter.isValidTournamentsOnly() && isTournamentBeforeToday(tournament)) {
            return false;
        }

        if (filter.onlyThisYearsTournaments() && !isTournamentInThisYear(tournament)) {
            return false;
        }

        if (!matchesAgeClass(filter.ageClasses(), tournament)) {
            return false;
        }

        if (!matchesAnyCheckedTournamentCategory(filter.tourCategories(), tournament)) {
            return false;
        }

        return matches(filter.name().orElse(null), tournament.tournamentName())
                && matches(filter.location().orElse(null), tournament.location());
    }

    private boolean matchesAgeClass(Set<AgeClass> filterAgeClasses, Tournament tournament) {
        if (filterAgeClasses == null || filterAgeClasses.isEmpty()) {
            return true;
        }
        return filterAgeClasses.stream().anyMatch(tournament::isForAgeClass);
    }

    private boolean matchesAnyCheckedTournamentCategory(Set<TourCategory> checkedAgeClasses, Tournament tournament) {
        if (checkedAgeClasses == null || checkedAgeClasses.isEmpty()) {
            return true;
        }
        return checkedAgeClasses.stream().anyMatch(tc -> tc.name().equalsIgnoreCase(tournament.tourCategory().getBaseCategory()));
    }

    private boolean matches(String filterValue, String actualValue) {
        if (filterValue == null || filterValue.isEmpty()) {
            return true;
        }
        return actualValue != null && actualValue.toLowerCase().contains(filterValue.toLowerCase());
    }

    private boolean isTournamentInThisYear(Tournament tournament) {
        log().trace("LoadTournamentsService  ::isTournamentInThisYear :: tournament = {}", tournament.startDate());
        return LocalDate.now().getYear() == parseDateToTournamentFormat(tournament.startDate()).getYear();
    }

    private boolean isTournamentBeforeToday(Tournament tournament) {
        log().trace("LoadTournamentsService ::isTournamentBeforeToday :: tournament = {}", tournament.startDate());
        var today = LocalDate.now();
        var tournamentDate = parseDateToTournamentFormat(tournament.startDate());
        return tournamentDate.isBefore(today);
    }
}
