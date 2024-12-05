package ca.mcmaster.cas735.acmepark.lot_management.dtos;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class IssueUserFine {
    String userId;
    Integer amount;
    String description;
    LocalDateTime issuedOn;
}
