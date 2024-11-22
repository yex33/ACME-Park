package ca.mcmaster.cas735.acmepark.member_identification.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransponderAccessData {
    private String transponderId;
    private String licensePlate;
    private String gateNumber;
}
