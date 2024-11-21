package ca.mcmaster.cas735.acmepark.lot_management.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class IssueVehicleFine {
    private String licensePlate;
    private Integer fine;

    public IssueVehicleFine(String licensePlate, Integer fine) {
        this.licensePlate = licensePlate;
        this.fine = fine;
    }
}
