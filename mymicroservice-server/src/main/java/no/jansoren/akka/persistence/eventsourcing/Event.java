package no.jansoren.akka.persistence.eventsourcing;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Event implements Serializable {

    private LocalDateTime created;

    public Event() {
        this.created = LocalDateTime.now();
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public abstract String getDescription();
}
