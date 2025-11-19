package de.ostfale.va.application.domain.model;

import java.time.LocalDate;

public record Tournament(
        String name,
        String location,
        LocalDate date
) {
}
