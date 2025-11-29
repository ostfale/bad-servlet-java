package de.ostfale.va.application.port.in;

import de.ostfale.va.common.UseLogging;

import java.util.Objects;
import java.util.Optional;

public class TournamentsFilter implements UseLogging {
    private final String location;
    private final String name;
    private final Integer offset;             // nullable -> no paging
    private final Integer limit;              // nullable -> no paging

    public TournamentsFilter(String location, String name, Integer offset, Integer limit) {
        this.offset = offset;
        this.limit = limit;
        log().debug("TournamentsFilter :: constructor");
        this.location = location;
        this.name = name;
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

    public Optional<Integer> offset() {
        return Optional.ofNullable(offset);
    }

    public Optional<Integer> limit() {
        return Optional.ofNullable(limit);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TournamentsFilter that = (TournamentsFilter) o;
        return Objects.equals(location, that.location) && Objects.equals(name, that.name) && Objects.equals(offset, that.offset) && Objects.equals(limit, that.limit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, name, offset, limit);
    }

    public static final class Builder {
        private String location;
        private String name;

        private Integer offset;
        private Integer limit;

        public Builder withLocation(String location) {
            this.location = location;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withOffset(Integer offset) {
            this.offset = offset;
            return this;
        }

        public Builder withLimit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public TournamentsFilter build() {
            return new TournamentsFilter(location, name, offset, limit);
        }
    }
}
