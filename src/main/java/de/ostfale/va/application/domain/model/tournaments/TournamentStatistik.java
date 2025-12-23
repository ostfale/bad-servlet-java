package de.ostfale.va.application.domain.model.tournaments;

import de.ostfale.va.common.UseLogging;

public record TournamentStatistik(
        int totalTournamentsThisYear,
        int totalTournamentsNextYear,
        int openTournamentsThisYear
) implements UseLogging {

    public int totalTournaments() {
        var totalTournaments = totalTournamentsThisYear + totalTournamentsNextYear;
        log().debug("TournamentStatistik :: Total tournaments: {}", totalTournaments);
        return totalTournaments;
    }
}
