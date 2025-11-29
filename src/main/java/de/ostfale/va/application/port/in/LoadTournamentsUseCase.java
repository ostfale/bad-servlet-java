package de.ostfale.va.application.port.in;

import de.ostfale.va.application.domain.model.Tournament;

import java.util.List;

public interface LoadTournamentsUseCase {

    List<Tournament> loadTournaments();

    List<Tournament> filter(TournamentsFilter filter);

    int count(TournamentsFilter filter);
}
