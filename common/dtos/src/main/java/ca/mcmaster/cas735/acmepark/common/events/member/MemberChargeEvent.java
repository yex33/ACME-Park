package ca.mcmaster.cas735.acmepark.common.events.member;

import ca.mcmaster.cas735.acmepark.common.dtos.InvoiceDto;
import ca.mcmaster.cas735.acmepark.common.events.Event;
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
