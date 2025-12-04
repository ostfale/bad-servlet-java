package de.ostfale.va.ui.tournament.controls;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import de.ostfale.va.application.domain.model.Tournament;
import de.ostfale.va.common.UseLogging;

public class TournamentDetailsComponent extends VerticalLayout implements UseLogging {

    private final FormLayout formLayout;
    private final TextField tournamentNameField;

    public TournamentDetailsComponent() {
        this.formLayout = new FormLayout();
        tournamentNameField = new TextField("Tournament Name");
        tournamentNameField.setReadOnly(true);
        formLayout.add(tournamentNameField);

        add(formLayout);
        setPadding(true);
    }

    public void setTournament(Tournament tournament) {
        if (tournament != null) {
            tournamentNameField.setValue(tournament.tournamentName());
        } else {
            tournamentNameField.clear();
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
}
