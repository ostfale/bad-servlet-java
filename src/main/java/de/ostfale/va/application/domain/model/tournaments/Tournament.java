package de.ostfale.va.application.domain.model.tournaments;

import de.ostfale.va.common.TimeHandlerFacade;

import java.time.LocalDate;
import java.util.List;

public record Tournament(
        String startDate,
        String endDate,
        String tournamentName,
        TournamentType tournamentType,
        int tournamentOrderNo,
        String countryCode,
        String location,
        String postalCode,
        String region,
        String openName,
        String organizer,
        TourCategory tourCategory,
        String closingDate,
        String webLinkUrl,
        String pdfLinkUrl,
        String pdfAvailable,
        String tourCreationDate,
        String tourVisibleDate,
        String invitationCreationDate,
        String tourLinkCreationDate,
        List<AgeClassDisciplines> ageClassDisciplines
) implements TimeHandlerFacade {

    public boolean isFromCurrentYear() {
        LocalDate startDate = parseDateToTournamentFormat(this.startDate);
        int thisYear = getActualCalendarYear();
        return startDate.getYear() == thisYear;
    }

    public boolean isFromNextYear() {
        LocalDate startDate = parseDateToTournamentFormat(this.startDate);
        int thisYear = getActualCalendarYear();
        return startDate.getYear() == thisYear + 1;
    }

    public boolean isOpenTournament() {
        LocalDate startDate = parseDateToTournamentFormat(this.startDate);
        return isFromCurrentYear() &&startDate.isAfter(LocalDate.now());
    }

    public boolean isForAgeClass(AgeClass ageClass) {
        return ageClassDisciplines.stream()
                .anyMatch(disciplines -> disciplines.ageClass().equals(ageClass) && disciplines.anyDisciplineForThisAgeClass());
    }
}
