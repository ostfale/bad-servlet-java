package de.ostfale.va.adapter.in.web.dashboard.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.ostfale.va.application.domain.events.EventBus;
import de.ostfale.va.application.domain.events.FilesDownloadedEvent;
import de.ostfale.va.application.domain.service.LastDownloadService;
import de.ostfale.va.common.UseLogging;

import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class DownloadInfoCard extends Div implements UseLogging {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final LastDownloadService downloadService;
    private final Span dateLabel;
    private Consumer<FilesDownloadedEvent> eventListener;

    public DownloadInfoCard() {
        log().info("DownloadInfoCard :: constructor");
        this.downloadService = new LastDownloadService();

        addClassName("card");
        getStyle()
                .set("padding", "1rem")
                .set("background", "white")
                .set("border-radius", "8px")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

        H3 title = new H3("Last Tournament Download");
        title.getStyle().set("margin", "0 0 0.5rem 0");

        this.dateLabel = new Span();
        dateLabel.getStyle()
                .set("font-size", "1.2rem")
                .set("font-weight", "bold")
                .set("color", "#1976d2");

        updateDateLabel();

        VerticalLayout layout = new VerticalLayout(title, dateLabel);
        layout.setPadding(false);
        layout.setSpacing(false);

        add(layout);
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

    private void updateDateLabel() {
        downloadService.getLastDownloadDate()
                .map(date -> "Downloaded on: " + date.format(DATE_FORMATTER))
                .ifPresentOrElse(
                        dateLabel::setText,
                        () -> dateLabel.setText("No downloads yet")
                );
    }
}
