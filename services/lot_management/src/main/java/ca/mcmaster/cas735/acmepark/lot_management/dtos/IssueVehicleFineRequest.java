package ca.mcmaster.cas735.acmepark.lot_management.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class IssueVehicleFineRequest {
    private String licensePlate;
    private Integer fine;
}
