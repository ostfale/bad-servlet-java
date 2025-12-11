package de.ostfale.va.application.port.in.tournaments;

import de.ostfale.va.application.domain.model.tournaments.AgeClass;
import de.ostfale.va.application.domain.model.tournaments.TourCategory;
import de.ostfale.va.common.UseLogging;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class TournamentsFilter implements UseLogging {
    private final String location;
    private final String name;
    private final boolean validTournamentsOnly;
    private final boolean onlyThisYearsTournaments;
    private final Set<AgeClass> ageClasses;
    private final Set<TourCategory> tourCategories;

    public TournamentsFilter(String location,
                             String name,
                             boolean validTournamentsOnly,
                             boolean onlyThisYearsTournaments,
                             Set<AgeClass> ageClasses,
                             Set<TourCategory> tourCategories
    ) {
        log().debug("TournamentsFilter :: constructor");
        this.location = location;
        this.name = name;
        this.validTournamentsOnly = validTournamentsOnly;
        this.onlyThisYearsTournaments = onlyThisYearsTournaments;
        this.ageClasses = (ageClasses != null) ? ageClasses : Collections.emptySet();
        this.tourCategories = (tourCategories != null) ? tourCategories : Collections.emptySet();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Optional<String> location() {
        return Optional.ofNullable(location);
    }

    public Optional<String> name() {
        return Optional.ofNullable(name);
    }

    public boolean isValidTournamentsOnly() {
        return validTournamentsOnly;
    }

    public boolean onlyThisYearsTournaments() {
        return onlyThisYearsTournaments;
    }

    public Set<AgeClass> ageClasses() {
        return ageClasses;
    }

    public Set<TourCategory> tourCategories() {
        return tourCategories;
    }

    @Override
    public String toString() {
        return String.format("""
                        TournamentsFilter:
                          valid tournaments: %b
                          only this year:    %b
                          name:              %s
                          location:          %s""",
                validTournamentsOnly, onlyThisYearsTournaments, name, location);
    }

    public static final class Builder {
        private String location;
        private String name;

        private boolean validTournamentsOnly;
        private boolean onlyThisYearsTournaments;
        private Set<AgeClass> ageClasses;
        private Set<TourCategory> tourCategories;

        public Builder withLocation(String location) {
            this.location = location;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withValidTournamentsOnly(boolean validTournamentsOnly) {
            this.validTournamentsOnly = validTournamentsOnly;
            return this;
        }

        public Builder withOnlyThisYearsTournaments(boolean onlyThisYearsTournaments) {
            this.onlyThisYearsTournaments = onlyThisYearsTournaments;
            return this;
        }

        public Builder withAgeClasses(Set<AgeClass> ageClasses) {
            this.ageClasses = ageClasses;
            return this;
        }

        public Builder withTourCategories(Set<TourCategory> tourCategories) {
            this.tourCategories = tourCategories;
            return this;
        }

        public TournamentsFilter build() {
            return new TournamentsFilter(location, name, validTournamentsOnly, onlyThisYearsTournaments, ageClasses, tourCategories);
        }
    }
}
