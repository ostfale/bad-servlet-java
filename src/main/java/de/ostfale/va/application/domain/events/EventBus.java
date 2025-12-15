package de.ostfale.va.application.domain.events;

import de.ostfale.va.common.UseLogging;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EventBus implements UseLogging {

    private static final EventBus INSTANCE = new EventBus();
    private final List<Consumer<FilesDownloadedEvent>> listeners = new ArrayList<>();

    private EventBus() {
    }

    public static EventBus getInstance() {
        return INSTANCE;
    }

    public void subscribe(Consumer<FilesDownloadedEvent> listener) {
        listeners.add(listener);
    }

    public void unsubscribe(Consumer<FilesDownloadedEvent> listener) {
        listeners.remove(listener);
    }

    public void publish(FilesDownloadedEvent event) {
        listeners.forEach(listener -> listener.accept(event));
    }
}
