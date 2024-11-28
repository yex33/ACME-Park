package ca.mcmaster.cas735.acmepark.visitor_identification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingFeeCreationData {
    private String visitorId;
    private Integer amount;
    private String gateId;
    private String licensePlate;
    private LocalDateTime timestamp;
}
