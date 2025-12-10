package de.ostfale.va.common;

import de.ostfale.va.adapter.out.LoadTournamentsPort;
import de.ostfale.va.adapter.out.persistence.CsvTournamentAdapter;
import de.ostfale.va.application.domain.service.FilterTournamentsService;
import de.ostfale.va.application.port.in.FilterTournamentsUseCase;

public class ServiceRegistry  implements UseLogging{

    private static final ServiceRegistry INSTANCE = new ServiceRegistry();

    private final FilterTournamentsUseCase filterTournamentsUseCase;

    private ServiceRegistry() {
        // 1. Create the Adapter (Implementation)
        LoadTournamentsPort tournamentPort = new CsvTournamentAdapter();

        // 2. Inject the Port (Interface) into the Service
        this.filterTournamentsUseCase = new FilterTournamentsService(tournamentPort.loadAll());

        // Note: Ideally, your Service constructor should accept the 'LoadTournamentsPort',
        // not the List<Tournament> directly, so the Service can decide when to load data.

        // Log initialization
        UseLogging.staticLogger().info("ServiceRegistry :: Services wired and initialized");
    }

    public static ServiceRegistry getInstance() {
        return INSTANCE;
    }

    // Expose only the INTERFACES (Ports), never the concrete classes
    public FilterTournamentsUseCase getFilterTournamentsUseCase() {
        return filterTournamentsUseCase;
    }
}
