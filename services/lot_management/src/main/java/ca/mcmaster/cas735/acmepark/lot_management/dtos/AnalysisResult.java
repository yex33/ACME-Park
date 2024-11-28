package ca.mcmaster.cas735.acmepark.lot_management.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter @Getter
@Builder
public class AnalysisResult {
    private long occupancy;
    private String gateId;
}
