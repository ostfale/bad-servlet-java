package de.ostfale.va.application.domain.service.tournament;

import de.ostfale.va.application.domain.model.tournaments.Tournament;
import de.ostfale.va.application.domain.model.tournaments.TournamentStatistik;
import de.ostfale.va.common.TimeHandlerFacade;
import de.ostfale.va.common.UseCase;
import de.ostfale.va.common.UseLogging;

import java.time.LocalDate;
import java.util.List;

@UseCase
public class CalculateTournamentsStatistikService implements UseLogging, TimeHandlerFacade {

    public TournamentStatistik loadTournamentsStatistik(List<Tournament> tournaments) {
        var totalTournamentsThisYear = calculateAllTournamentsThisYear(tournaments);
        var totalTournamentsNextYear = calculateAllTournamentsNextYear(tournaments);
        var openTournamentsThisYear = calculateAllOpenTournamentsThisYear(tournaments);

        return new TournamentStatistik(totalTournamentsThisYear,
                totalTournamentsNextYear,
                openTournamentsThisYear);
    }

    private int calculateAllTournamentsThisYear(List<Tournament> tournaments) {
        var currentYear = getActualCalendarYear();
        var result = (int) tournaments.stream()
                .filter(t -> parseDateToTournamentFormat(t.startDate()).getYear() == currentYear)
                .count();
        log().debug("CalculateTournamentsStatistikService :: Calculating tournaments for this year: {}", result);
        return result;
    }

    private int calculateAllOpenTournamentsThisYear(List<Tournament> tournaments) {
        var currentYear = getActualCalendarYear();
        var today = LocalDate.now();
        log().debug("CalculateTournamentsStatistikService :: Calculating open tournaments for this year: {}", currentYear);
        return (int) tournaments.stream()
                .filter(t -> parseDateToTournamentFormat(t.startDate()).getYear() == currentYear
                        && parseDateToTournamentFormat(t.startDate()).isAfter(today))
                .count();
    }

    private int calculateAllTournamentsNextYear(List<Tournament> tournaments) {
        var nextYear = getActualCalendarYear() + 1;
        log().debug("CalculateTournamentsStatistikService :: Calculating tournaments for next year: {}", nextYear);
        return (int) tournaments.stream()
                .filter(t -> parseDateToTournamentFormat(t.startDate()).getYear() == nextYear)
                .count();
    }
}
