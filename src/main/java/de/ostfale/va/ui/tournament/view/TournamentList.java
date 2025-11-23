package de.ostfale.va.ui.tournament.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import de.ostfale.va.application.domain.model.Tournament;
import de.ostfale.va.common.UseLogging;

import java.util.List;

public class TournamentList extends VerticalLayout implements UseLogging {

    private final Grid<Tournament> grid;

    public TournamentList(List<Tournament> tournaments) {
        log().debug("TournamentList :: Init tournament list view");
        Paragraph paragraph = new Paragraph("Alle aktiv geplanten Turniere des DBV");
        grid = new Grid<>();
        grid.addClassName("tournament-grid"); // Add this line
        grid.addColumn(Tournament::startDate).setHeader("Datum");
        grid.addColumn(Tournament::closedDate).setHeader("Meldeschluss");
        grid.addColumn(Tournament::tournamentName).setHeader("Turniername");
        grid.addColumn(Tournament::location).setHeader("Ort");
        grid.addColumn(Tournament::categoryName).setHeader("Kategorie");
        grid.addColumn(Tournament::organizer).setHeader("Organisation");
        grid.addColumn(new ComponentRenderer<>(tournament ->
                createLinkComponent(tournament.webLinkUrl(), VaadinIcon.LINK.create())
        )).setHeader("DBV Turnier");

        grid.addColumn(new ComponentRenderer<>(tournament ->
                createLinkComponent(tournament.pdfLinkUrl(), VaadinIcon.FILE_TEXT_O.create())

        )).setHeader("Ausschreibung");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setItems(tournaments);
        grid.setHeightFull();
        add(paragraph, grid);
    }

    public Grid<Tournament> getGrid() {
        return grid;
    }

    private Component createLinkComponent(String url, Component icon) {
        if (url != null && !url.isEmpty()) {
            Anchor link = new Anchor(url, icon);
            link.setTarget("_blank");
            return link;
        }
        return new Span("-");
    }
}
