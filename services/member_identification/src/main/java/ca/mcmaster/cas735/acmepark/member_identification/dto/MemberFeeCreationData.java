package ca.mcmaster.cas735.acmepark.member_identification.dto;

import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberFeeCreationData {
    private String organizationId;
    private UserType userType;
    private Integer amount;
    private LocalDateTime timestamp;
    private String description;
    private String associatedPermitId;
}
