package de.ostfale.va.application.domain.service;

import com.vaadin.signals.ValueSignal;
import de.ostfale.va.application.domain.model.tournaments.TournamentStatistics;
import de.ostfale.va.common.UseCase;
import de.ostfale.va.common.UseLogging;

@UseCase
public class TournamentStatisticsSignalService implements UseLogging {

    private static final TournamentStatisticsSignalService INSTANCE = new TournamentStatisticsSignalService();
    private final ValueSignal<TournamentStatistics> statisticsSignal;

    private TournamentStatisticsSignalService() {
        this.statisticsSignal = new ValueSignal<>(TournamentStatistics.class);

        // Initialize with default values
        statisticsSignal.value(new TournamentStatistics("Keine Downloads vorhanden", 0, 0, 0));

        log().info("TournamentStatisticsSignalService :: Initialized with tournament statistics signal");
    }

    public static TournamentStatisticsSignalService getInstance() {
        return INSTANCE;
    }

    public ValueSignal<TournamentStatistics> getStatisticsSignal() {
        return statisticsSignal;
    }

    public void updateStatistics(TournamentStatistics statistics) {
        statisticsSignal.value(statistics);

        log().debug("TournamentStatisticsSignalService :: Updated statistics - Date: {}, This Year: {}, Next Year: {}, Open: {}",
                statistics.lastDownloadDate(),
                statistics.totalTournamentsThisYear(),
                statistics.totalTournamentsNextYear(),
                statistics.openTournamentsThisYear());
    }
}
