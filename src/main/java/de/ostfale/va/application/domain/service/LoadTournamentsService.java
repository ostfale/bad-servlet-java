package de.ostfale.va.application.domain.service;

import de.ostfale.va.application.domain.model.AgeClass;
import de.ostfale.va.application.domain.model.Tournament;
import de.ostfale.va.application.port.in.LoadTournamentsUseCase;
import de.ostfale.va.application.port.in.TournamentsFilter;
import de.ostfale.va.common.UseLogging;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class LoadTournamentsService implements LoadTournamentsUseCase, UseLogging {

    private final List<Tournament> tournaments;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public LoadTournamentsService(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }

    @Override
    public List<Tournament> getAllTournaments() {
        return List.of();
    }

    @Override
    public Stream<Tournament> fetch(TournamentsFilter filter, int offset, int limit) {
        return tournaments.stream()
                .filter(tournament -> matchesFilter(tournament, filter))
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

    private boolean matchesFilter(Tournament tournament, TournamentsFilter filter) {
        if (filter == null) {
            return true;
        }

        if (filter.isValidTournamentsOnly() && isTournamentBeforeToday(tournament)) {
            return false;
        }

        if (!matchesAgeClass(filter.ageClasses(), tournament)) {
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

    private boolean matches(String filterValue, String actualValue) {
        if (filterValue == null || filterValue.isEmpty()) {
            return true;
        }
        return actualValue != null && actualValue.toLowerCase().contains(filterValue.toLowerCase());
    }

    private boolean isTournamentBeforeToday(Tournament tournament) {
        log().trace("isTournamentBeforeToday :: tournament = {}", tournament.startDate());
        var today = LocalDate.now();
        var tournamentDate = LocalDate.parse(tournament.startDate(), formatter);
        return tournamentDate.isBefore(today);
    }
}
