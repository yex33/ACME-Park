package ca.mcmaster.cas735.acmepark.lot_management.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data @NoArgsConstructor
public class IssueVehicleFine {
    private String licensePlate;
    private String fine;
}
