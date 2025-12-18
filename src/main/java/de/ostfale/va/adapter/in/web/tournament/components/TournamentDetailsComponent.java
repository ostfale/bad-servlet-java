package de.ostfale.va.adapter.in.web.tournament.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import de.ostfale.va.application.domain.model.tournaments.Tournament;
import de.ostfale.va.common.UseLogging;

public class TournamentDetailsComponent extends VerticalLayout implements UseLogging {

    private static final String TOURNAMENT_NAME_LABEL = "Name";
    private static final String TOURNAMENT_LOCATION_LABEL = "Ort";
    private static final String TOURNAMENT_DATE_LABEL = "Datum";
    private static final String TOURNAMENT_CLOSING_DATE_LABEL = "Meldeschluss";
    private static final String TOURNAMENT_ORGANIZATION_LABEL = "Veranstalter";
    private static final String TOURNAMENT_COUNTRYN_LABEL = "Land";
    private static final String TOURNAMENT_CATEGORY_LABEL = "Kategore";

    private final TextField tournamentNameField;
    private final TextField tournamentLocationField;
    private final TextField tournamentDateField;
    private final TextField tournamentClosingDateField;
    private final TextField tournamentOrganizationField;
    private final TextField tournamentCountryField;
    private final TextField tournamentCategoryField;


    public TournamentDetailsComponent() {
        log().info("TournamentDetailsComponent :: constructor");
        tournamentNameField = createTextField(TOURNAMENT_NAME_LABEL, "300px");
        tournamentLocationField = createTextField(TOURNAMENT_LOCATION_LABEL, "200px");
        tournamentDateField = createTextField(TOURNAMENT_DATE_LABEL, "200px");
        tournamentClosingDateField = createTextField(TOURNAMENT_CLOSING_DATE_LABEL, "300px");
        tournamentOrganizationField = createTextField(TOURNAMENT_ORGANIZATION_LABEL, "300px");
        tournamentCountryField = createTextField(TOURNAMENT_COUNTRYN_LABEL, "300px");
        tournamentCategoryField = createTextField(TOURNAMENT_CATEGORY_LABEL, "300px");

        FormLayout formLayout = new FormLayout();
        formLayout.setWidthFull();
        formLayout.setExpandColumns(true);
        formLayout.addFormRow(tournamentNameField);
        formLayout.addFormRow(tournamentLocationField, tournamentOrganizationField);
        formLayout.addFormRow(tournamentDateField, tournamentClosingDateField);
        formLayout.addFormRow(tournamentOrganizationField, tournamentCategoryField);

        add(formLayout);
        setPadding(true);
    }

    public void setTournament(Tournament tournament) {
        if (tournament != null) {
            tournamentNameField.setValue(tournament.tournamentName());
            tournamentLocationField.setValue(tournament.location());
            tournamentDateField.setValue(tournament.startDate());
            tournamentClosingDateField.setValue(tournament.closingDate());
            tournamentOrganizationField.setValue(tournament.organizer());
            tournamentCountryField.setValue(tournament.countryCode());
            tournamentCategoryField.setValue(tournament.tourCategory().getBaseCategory());
        } else {
            tournamentNameField.clear();
            tournamentLocationField.clear();
            tournamentDateField.clear();
            tournamentClosingDateField.clear();
            tournamentOrganizationField.clear();
            tournamentCountryField.clear();
            tournamentCategoryField.clear();
        }
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

    public static class CloseEvent extends ComponentEvent<TournamentDetailsComponent> {
        public CloseEvent(TournamentDetailsComponent source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    private TextField createTextField(String label, String width) {
        TextField textField = new TextField(label);
        textField.setWidth(width);
        textField.setReadOnly(true);
        return textField;
    }
}
