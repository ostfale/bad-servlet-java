package de.ostfale.va.adapter.in.web.dashboard.components;

import com.vaadin.flow.component.ComponentEffect;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.ostfale.va.application.domain.service.TournamentStatisticsSignalService;
import de.ostfale.va.common.TimeHandlerFacade;
import de.ostfale.va.common.UseLogging;

public class DownloadInfoCard extends Div implements UseLogging, TimeHandlerFacade {

    private final Span dateLabel;
    private final Span currentYearValueLabel;
    private final Span nextYearValueLabel;

    public DownloadInfoCard(TournamentStatisticsSignalService statisticsSignalService) {
        log().info("DownloadInfoCard :: constructor");

        this.dateLabel = createDateLabel();
        this.currentYearValueLabel = new Span("- / -");
        this.nextYearValueLabel = new Span("-");

        currentYearValueLabel.addClassName("property-value");
        nextYearValueLabel.addClassName("property-value");

        initLayout();

        // Register effect in constructor - Vaadin 25 handles lifecycle automatically
        var signal = statisticsSignalService.getStatisticsSignal();
        ComponentEffect.effect(this, () -> {
            var stats = signal.value();
            dateLabel.setText(stats.lastDownloadDate());
            currentYearValueLabel.setText(stats.openTournamentsThisYear() + " / " + stats.totalTournamentsThisYear());
            nextYearValueLabel.setText(String.valueOf(stats.totalTournamentsNextYear()));
        });

        log().info("DownloadInfoCard :: Signal effect registered");
    }

    private void initLayout() {
        Div layout = createLayout();
        Card tournamentImageCard = createCard();
        Image image = createImage();

        // create the card title
        var thisYear = getActualCalendarYear();
        H2 sectionTitle = new H2("Statistik " + thisYear + "/" + ++thisYear);
        sectionTitle.getStyle().set("margin", "0");

        // Layout to push title to left and year to right
        HorizontalLayout yearHeader = new HorizontalLayout(sectionTitle);
        yearHeader.setWidthFull();
        yearHeader.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        yearHeader.setAlignItems(FlexComponent.Alignment.BASELINE);
        yearHeader.getStyle().set("padding", "0 1rem");

        VerticalLayout resultLayout = new VerticalLayout();
        resultLayout.add(yearHeader, prepareDownloadRow(), prepareCurrentYearRow(), prepareNextYearRow());
        tournamentImageCard.add(image, resultLayout);

        layout.add(tournamentImageCard);
        add(layout);
    }


    private HorizontalLayout prepareDownloadRow() {
        H2 title = new H2("Letzter Download: ");
        title.addClassName("download-title");

        HorizontalLayout hl = new HorizontalLayout();
        hl.setWidthFull();
        hl.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        hl.setSpacing(true); // Adds a little gap between "Letzter Download" and the date
        hl.getStyle().set("padding-right", "1rem");

        hl.add(title, this.dateLabel);
        return hl;
    }

    private HorizontalLayout prepareCurrentYearRow() {
        HorizontalLayout row = new HorizontalLayout();
        row.setWidthFull();
        row.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        row.getStyle().set("padding-right", "1rem");

        Span nameLabel = new Span("Turniere " + getActualCalendarYear());
        nameLabel.addClassName("property-name");

        row.add(nameLabel, currentYearValueLabel);
        return row;
    }

    private HorizontalLayout prepareNextYearRow() {
        HorizontalLayout row = new HorizontalLayout();
        row.setWidthFull();
        row.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        row.getStyle().set("padding-right", "1rem");

        Span nameLabel = new Span("Turniere " + getNextCalendarYear());
        nameLabel.addClassName("property-name");

        row.add(nameLabel, nextYearValueLabel);
        return row;
    }

    private Div createLayout() {
        Div layout = new Div();
        layout.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fill, minmax(190px, 1fr))")
                .set("gap", "1em")
                .set("padding", "1rem");
        return layout;
    }

    private Span createDateLabel() {
        Span label = new Span();
        addClassName("download-date-label");
        return label;
    }

    private Card createCard() {
        var card = new Card();
        card.addClassName("download-info-card");
        return card;
    }

    private Image createImage() {
        var tournamentImage = new Image("images/tournaments_card.png", "Tournaments");
        tournamentImage.setWidth("100%");
        return tournamentImage;
    }
}
