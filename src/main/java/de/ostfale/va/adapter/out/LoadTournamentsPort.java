package de.ostfale.va.adapter.out;

import de.ostfale.va.application.domain.model.Tournament;

import java.util.List;

public interface LoadTournamentsPort {

    List<Tournament> loadAll();
}
