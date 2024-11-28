package ca.mcmaster.cas735.acmepark.parking_enforcement.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
@Jacksonized
public class FineEvent {
    UUID userId;
    String violation;
    LocalDateTime issuedOn;
}
