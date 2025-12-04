package de.ostfale.va.application.domain.service;

import de.ostfale.va.application.domain.model.Tournament;
import de.ostfale.va.application.port.in.LoadTournamentsUseCase;
import de.ostfale.va.application.port.in.TournamentsFilter;
import de.ostfale.va.common.UseLogging;

import java.util.List;
import java.util.Objects;

public class LoadTournamentsService implements LoadTournamentsUseCase, UseLogging{

    private final List<Tournament> tournaments;

    public LoadTournamentsService(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }

    public List<Tournament> loadTournaments() {
        return tournaments;
    }

    @Override
    public List<Tournament> filter(TournamentsFilter filter) {
        Objects.requireNonNull(filter, "'filter' must not be null");
        List<Tournament> filteredTournaments = tournaments.stream()
                .filter(tournament -> matchesFilter(tournament, filter))
                .toList();

        log().debug("Applied filter: {} tournaments found", filteredTournaments.size());
        return filteredTournaments;
    }

    @Override
    public int count(TournamentsFilter filter) {
        return filter(filter).size();
    }

    private boolean matchesFilter(Tournament tournament, TournamentsFilter filter) {

        // Check name filter
        if (filter.name().isPresent() && !filter.name().get().isEmpty()) {
            String nameFilter = filter.name().get().toLowerCase();
            if (!tournament.tournamentName().toLowerCase().contains(nameFilter)) {
                return false;
            }
        }

        // Check location filter
        if (filter.location().isPresent() && !filter.location().get().isEmpty()) {
            String locationFilter = filter.location().get().toLowerCase();
            return tournament.location().toLowerCase().contains(locationFilter);
        }

        return true;
    }
}
