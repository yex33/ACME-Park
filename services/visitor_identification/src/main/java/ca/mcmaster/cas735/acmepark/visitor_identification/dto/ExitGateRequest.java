package ca.mcmaster.cas735.acmepark.visitor_identification.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class ExitGateRequest {
    private String license;
    private String gateId;
}
