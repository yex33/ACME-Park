package ca.mcmaster.cas735.acmepark.common.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class AccessGateRequest {
    private String userId;
    private UserType userType;
    private String gateId;
}
