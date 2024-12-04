package ca.mcmaster.cas735.acmepark.lot_management.dtos;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class IssueVehicleFine {
    String description;
    Integer amount;
}
