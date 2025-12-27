package de.ostfale.va.application.domain.model.tournaments;

import de.ostfale.va.common.UseLogging;

public record TournamentStatistics(
        String lastDownloadDate,
        long totalTournamentsThisYear,
        long totalTournamentsNextYear,
        long openTournamentsThisYear
) implements UseLogging {

    public long totalTournaments() {
        var totalTournaments = totalTournamentsThisYear + totalTournamentsNextYear;
        log().debug("TournamentStatistik :: Total tournaments: {}", totalTournaments);
        return totalTournaments;
    }
}
