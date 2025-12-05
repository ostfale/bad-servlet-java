package de.ostfale.va.application.port.in;

import de.ostfale.va.application.domain.model.Tournament;

import java.util.List;
import java.util.stream.Stream;

public interface LoadTournamentsUseCase {

    List<Tournament> getAllTournaments();

    Stream<Tournament> fetch(TournamentsFilter filter, int offset, int limit);

    int count(TournamentsFilter filter);
}
