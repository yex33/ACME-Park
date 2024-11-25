package ca.mcmaster.cas735.acmepark.visitor_identification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitorExitData {
    private String visitorId;
    private String gateId;
    private String licensePlate;
    private String voucherId;
}
