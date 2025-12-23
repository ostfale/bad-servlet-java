package de.ostfale.va.adapter.in.web.dashboard.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.ostfale.va.application.domain.events.EventBus;
import de.ostfale.va.application.domain.events.FilesDownloadedEvent;
import de.ostfale.va.application.domain.service.LastDownloadService;
import de.ostfale.va.common.TimeHandlerFacade;
import de.ostfale.va.common.UseLogging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class DownloadInfoCard extends Div implements UseLogging, TimeHandlerFacade {

    private final LastDownloadService downloadService;
    private final Span dateLabel;
    private Consumer<FilesDownloadedEvent> eventListener;

    public DownloadInfoCard() {
        log().info("DownloadInfoCard :: constructor");

        this.dateLabel = createDateLabel();
        this.downloadService = new LastDownloadService();
        initLayout();
        updateDateLabel();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Subscribe to download events
        eventListener = event -> {
            attachEvent.getUI().access(this::updateDateLabel);
        };
        EventBus.getInstance().subscribe(eventListener);
        log().info("DownloadInfoCard :: Subscribed to download events");
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        // Unsubscribe when component is removed
        if (eventListener != null) {
            EventBus.getInstance().unsubscribe(eventListener);
            log().info("DownloadInfoCard :: Unsubscribed from download events");
        }
    }

    private void initLayout() {
        Div layout = createLayout();
        Card tournamentImageCard = createCard();
        Image image = createImage();

        // create the card title
        var thisYear = getActualCalendarYear();
        H2 sectionTitle = new H2("Statistik " + thisYear + "/" + ++thisYear);
        sectionTitle.getStyle().set("margin", "0");


        //



        // Layout to push title to left and year to right
        HorizontalLayout yearHeader = new HorizontalLayout(sectionTitle);
        yearHeader.setWidthFull();
        yearHeader.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        yearHeader.setAlignItems(FlexComponent.Alignment.BASELINE);
        yearHeader.getStyle().set("padding", "0 1rem");

        VerticalLayout resultLayout = new VerticalLayout();
        resultLayout.add(yearHeader, prepareDownloadRow(),prepareCurrentYearRow(), prepareNextYearRow());
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
        H4 title = new H4("Turniere " + getActualCalendarYear());
        title.addClassName("year-title");
        return new HorizontalLayout(title);
    }

    private HorizontalLayout prepareNextYearRow() {
        H4 title = new H4("Turniere " + (getActualCalendarYear() +1));
        title.addClassName("year-title");
        return new HorizontalLayout(title);
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

    private void updateDateLabel() {
        downloadService.getLastDownloadDate()
                .map(date -> date.format(DateTimeFormatter.ofPattern(TOURNAMENT_DATE_DISPLAY_FORMAT)))
                .ifPresentOrElse(
                        dateLabel::setText,
                        () -> dateLabel.setText("Keine Downloads vorhanden")
                );
    }
}
