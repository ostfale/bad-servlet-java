package de.ostfale.va.application.domain.events;

import java.time.LocalDateTime;

public record FilesDownloadedEvent(
        LocalDateTime timestamp
) {
}
