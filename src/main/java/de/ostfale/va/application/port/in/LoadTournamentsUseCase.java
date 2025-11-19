package de.ostfale.va.application.port.in;

import de.ostfale.va.application.domain.model.Tournament;

import java.util.List;

public interface LoadTournamentsUseCase {

    List<Tournament> loadTournaments();
}
