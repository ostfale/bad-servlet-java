package de.ostfale.va.application.port.in;

import de.ostfale.va.application.domain.model.tournaments.Tournament;
import de.ostfale.va.application.domain.model.tournaments.TournamentStatistik;

import java.util.List;

public interface CalculateTournamentsStatisticsUseCase {

    TournamentStatistik calculateTournamentsStatistik(List<Tournament> tournaments);
}
