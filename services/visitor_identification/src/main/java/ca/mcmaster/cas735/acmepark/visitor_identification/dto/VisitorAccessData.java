package ca.mcmaster.cas735.acmepark.visitor_identification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitorAccessData {
    private String licensePlate;
    private String gateId;
}
