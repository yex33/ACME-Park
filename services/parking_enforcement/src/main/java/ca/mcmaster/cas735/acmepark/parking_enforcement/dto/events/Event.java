package ca.mcmaster.cas735.acmepark.parking_enforcement.dto.events;

import java.time.LocalDateTime;
import java.util.UUID;

public interface Event {
    UUID getEventId();
    LocalDateTime getDateTime();
}
