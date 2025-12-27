package de.ostfale.va.application.domain.service.tournament;

import de.ostfale.va.application.domain.model.tournaments.Tournament;
import de.ostfale.va.application.domain.model.tournaments.TournamentStatistics;
import de.ostfale.va.common.UseCase;
import de.ostfale.va.common.UseLogging;

import java.util.List;

@UseCase
public class CalculateTournamentsStatistikService implements UseLogging {

    public TournamentStatistics loadTournamentsStatistik(List<Tournament> tournaments, String lastDownloadDate) {
        var totalTournamentsThisYear = calculateAllTournamentsThisYear(tournaments);
        var totalTournamentsNextYear = calculateAllTournamentsNextYear(tournaments);
        var openTournamentsThisYear = calculateAllOpenTournamentsThisYear(tournaments);

        return new TournamentStatistics(lastDownloadDate,
                totalTournamentsThisYear,
                totalTournamentsNextYear,
                openTournamentsThisYear);
    }

    private long calculateAllTournamentsThisYear(List<Tournament> tournaments) {
        var result = tournaments.stream()
                .filter(Tournament::isFromCurrentYear)
                .count();
        log().debug("CalculateTournamentsStatistikService :: Calculating tournaments for this year: {}", result);
        return result;
    }

    private long  calculateAllOpenTournamentsThisYear(List<Tournament> tournaments) {
        var result = tournaments.stream()
                .filter(Tournament::isOpenTournament)
                .count();
        log().debug("CalculateTournamentsStatistikService :: Calculating open tournaments for this year: {}", result);
        return result;
    }

    private long calculateAllTournamentsNextYear(List<Tournament> tournaments) {
        var result = tournaments.stream()
                .filter(Tournament::isFromNextYear)
                .count();
        log().debug("CalculateTournamentsStatistikService :: Calculating tournaments for next year: {}", result);
        return result;
    }
}
