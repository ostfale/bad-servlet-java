package de.ostfale.va.application.domain.model.tournaments;


public record AgeClassDisciplines(
        AgeClass ageClass,
        boolean isSingle,
        boolean isDouble,
        boolean isMixed
) {

    public boolean anyDisciplineForThisAgeClass() {
        return isSingle || isDouble || isMixed;
    }
}
