package ca.mcmaster.cas735.acmepark.parking_enforcement.dto.events.fine;

import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.InvoiceDto;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.events.Event;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class FineEvent implements Event {
    UUID eventId = UUID.randomUUID();
    LocalDateTime dateTime = LocalDateTime.now();
    InvoiceDto invoice;
    FineStatus fineStatus;
}
