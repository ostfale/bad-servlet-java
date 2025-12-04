package de.ostfale.va.ui.tournament.controls;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import de.ostfale.va.application.port.in.TournamentsFilter;
import de.ostfale.va.common.UseLogging;

public class TournamentFilterComponent extends VerticalLayout implements UseLogging {

    private static final String TOURNAMENT_VIEW_NAME = "Termine des DBV";
    private static final String TOURNAMENT_NAME_FILTER = "Turniername";
    private static final String TOURNAMENT_NAME_PLACEHOLDER = "Name eingeben";
    private static final String LOCATION_NAME_FILTER = "Turnierort";
    private static final String LOCATION_NAME_PLACEHOLDER = "Ort eingeben";

    private static final String FILTER_BUTTON_LABEL = "Filter";
    private static final String RESET_BUTTON_LABEL = "Reset";

    private static final String FIELD_WIDTH = "15%";

    // filter label
    private final TextField nameFilter = new TextField(TOURNAMENT_NAME_FILTER);
    private final TextField locationFilter = new TextField(LOCATION_NAME_FILTER);

    private final Button applyButton = new Button(FILTER_BUTTON_LABEL);
    private final Button clearButton = new Button(RESET_BUTTON_LABEL);

    public TournamentFilterComponent() {
        log().debug("TournamentFilterPanel :: constructor");
        initLayoutSettings();
        add(createTitle(), createFilterLayout(), createButtonLayout());
    }

    public TournamentsFilter getCurrentFilter() {
        return TournamentsFilter.builder()
                .withName(nameFilter.getValue())
                .withLocation(locationFilter.getValue())
                .build();
    }

    public Registration addFilterChangeListener(ComponentEventListener<FilterChangeEvent> listener) {
        return addListener(FilterChangeEvent.class, listener);
    }

    private void initLayoutSettings() {
        setSpacing(false);
        setPadding(true);
        setWidthFull();
    }

    private Component createTitle() {
        Paragraph title = new Paragraph(TOURNAMENT_VIEW_NAME);
        title.setId("view-title");
        return title;
    }

    private Component createFilterLayout() {
        nameFilter.setPlaceholder(TOURNAMENT_NAME_PLACEHOLDER);
        nameFilter.setWidth(FIELD_WIDTH);
        locationFilter.setPlaceholder(LOCATION_NAME_PLACEHOLDER);
        locationFilter.setWidth(FIELD_WIDTH);

        HorizontalLayout textFields = new HorizontalLayout(nameFilter, locationFilter);
        textFields.setSpacing(true);
        textFields.setWidthFull();
        return textFields;
    }

    private Component createButtonLayout() {
        applyButton.setIcon(VaadinIcon.FILTER.create());
        applyButton.addClickListener(e -> fireFilterChangeEvent());

        clearButton.setIcon(VaadinIcon.CLOSE_CIRCLE.create());
        clearButton.addClickListener(e -> clearFilters());

        HorizontalLayout buttons = new HorizontalLayout(applyButton, clearButton);
        buttons.setSpacing(true);
        buttons.setJustifyContentMode(JustifyContentMode.END);
        return buttons;
    }

    private void clearFilters() {
        nameFilter.clear();
        locationFilter.clear();
        fireFilterChangeEvent();
    }

    private void fireFilterChangeEvent() {
        fireEvent(new FilterChangeEvent(this, false, getCurrentFilter()));
    }

    public static class FilterChangeEvent extends ComponentEvent<TournamentFilterComponent> {
        private final TournamentsFilter filter;

        public FilterChangeEvent(TournamentFilterComponent source, boolean fromClient, TournamentsFilter filter) {
            super(source, fromClient);
            this.filter = filter;
        }

        public TournamentsFilter getFilter() {
            return filter;
        }
    }

}
