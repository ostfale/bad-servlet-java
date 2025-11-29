package de.ostfale.va.application.domain.model;

import java.util.Arrays;

public enum TournamentType {

    Ranking("Rangliste"),
    Championship("Meisterschaft");

    private final String displayString;

    TournamentType(String displayString) {
        this.displayString = displayString;
    }

    public static TournamentType lookup(String aValue) {
        return Arrays.stream(values())
                .filter(kind -> kind.displayString.equalsIgnoreCase(aValue))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unknown TournamentType: " + aValue));
    }
}
