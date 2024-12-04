package ca.mcmaster.cas735.acmepark.parking_enforcement.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class FineEvent {
    String userId;
    Integer amount;
    String description;
    LocalDateTime issuedOn;
}
