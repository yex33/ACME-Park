package ca.mcmaster.cas735.acmepark.parking_enforcement.dto;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class FineEvent {
    UUID userId;
    Integer amount;
    LocalDateTime issuedOn;
}
