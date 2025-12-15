package de.ostfale.va.application.port.out;

import de.ostfale.va.application.domain.model.tournaments.Tournament;

import java.util.Collection;
import java.util.List;

public interface LoadTournamentsPort {

    List<Tournament> loadAll();
}
