package de.ostfale.va.application.port.in;

import de.ostfale.va.application.domain.model.tournaments.Tournament;
import de.ostfale.va.application.domain.model.tournaments.TournamentsFilter;

import java.util.stream.Stream;

public interface FilterTournamentsUseCase {

    Stream<Tournament> fetch(TournamentsFilter filter, int offset, int limit);

    int count(TournamentsFilter filter);
}
