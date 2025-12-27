package de.ostfale.va.application.port.in;

import de.ostfale.va.application.domain.model.tournaments.Tournament;
import de.ostfale.va.application.domain.model.tournaments.TournamentStatistics;

import java.util.List;

public interface CalculateTournamentsStatisticsUseCase {

    TournamentStatistics calculateTournamentsStatistik(List<Tournament> tournaments);
}
