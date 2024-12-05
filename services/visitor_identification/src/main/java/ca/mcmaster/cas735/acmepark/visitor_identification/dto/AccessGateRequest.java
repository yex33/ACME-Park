package ca.mcmaster.cas735.acmepark.visitor_identification.dto;

import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccessGateRequest {
    private String userId;
    private UserType userType;
    private String gateId;
    private String license;
}
