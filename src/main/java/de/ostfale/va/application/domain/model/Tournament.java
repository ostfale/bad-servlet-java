package de.ostfale.va.application.domain.model;

import java.time.LocalDate;
import java.util.List;

public record Tournament(
        LocalDate startDate,
        LocalDate closedDate,
        String tournamentName,
        String categoryName,
        String location,
        String organizer,
        String webLinkUrl,
        String pdfLinkUrl,
        List<AgeClassDisciplines> ageClassDisciplines
) {
}
