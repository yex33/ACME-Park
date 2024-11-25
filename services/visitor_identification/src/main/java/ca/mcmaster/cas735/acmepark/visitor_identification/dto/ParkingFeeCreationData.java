package ca.mcmaster.cas735.acmepark.visitor_identification.dto;

import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
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
    private LocalDateTime timestamp;
}
