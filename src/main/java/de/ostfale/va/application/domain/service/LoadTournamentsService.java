package de.ostfale.va.application.domain.service;

import de.ostfale.va.application.domain.model.Tournament;
import de.ostfale.va.application.port.in.LoadTournamentsFromMemory;
import de.ostfale.va.application.port.in.LoadTournamentsUseCase;
import de.ostfale.va.common.UseLogging;

import java.util.List;

public class LoadTournamentsService implements UseLogging{

    private final LoadTournamentsUseCase loadTournamentsUseCase;

    public LoadTournamentsService(LoadTournamentsUseCase loadTournamentsUseCase) {
        log().info("LoadTournamentsService :: constructor");
        this.loadTournamentsUseCase = loadTournamentsUseCase;
    }

    public List<Tournament> loadTournaments() {
        return loadTournamentsUseCase.loadTournaments();
    }
}
