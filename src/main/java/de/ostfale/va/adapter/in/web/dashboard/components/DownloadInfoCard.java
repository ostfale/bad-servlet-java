package de.ostfale.va.adapter.in.web.dashboard.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import de.ostfale.va.application.domain.events.EventBus;
import de.ostfale.va.application.domain.events.FilesDownloadedEvent;
import de.ostfale.va.application.domain.service.LastDownloadService;
import de.ostfale.va.common.TimeHandlerFacade;
import de.ostfale.va.common.UseLogging;

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

        H3 title = new H3("Letzter Download: ");
        title.getStyle()
                .set("color", "#ae1732")
                .set("font-weight", "normal")
                .set("margin", "0")
                .set("padding-left", "1rem")
                .set("padding-top", "1rem");

        HorizontalLayout header = new HorizontalLayout(title, dateLabel);
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.getStyle().set("padding-left", "1rem").set("padding-right", "1rem");

        tournamentImageCard.add(image, header);

        layout.add(tournamentImageCard);
        add(layout);
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
        label.getStyle()
                .set("color", "black")
                .set("padding-top", "1rem");
        return label;
    }

    private Card createCard() {
        var card = new Card();
        card.setWidth("600px");
        card.setHeight("600px");
        card.getStyle().set("padding", "0");
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
