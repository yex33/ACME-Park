package ca.mcmaster.cas735.acmepark.parking_enforcement.dto.events.member;

import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.InvoiceDto;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.events.Event;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class MemberChargeEvent implements Event {
    UUID eventId = UUID.randomUUID();
    LocalDateTime dateTime = LocalDateTime.now();
    InvoiceDto invoice;
    MemberChargeStatus chargeStatus;
}
