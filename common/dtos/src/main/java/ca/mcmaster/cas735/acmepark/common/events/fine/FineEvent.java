package ca.mcmaster.cas735.acmepark.common.events.fine;

import ca.mcmaster.cas735.acmepark.common.dtos.InvoiceDto;
import ca.mcmaster.cas735.acmepark.common.events.Event;
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
