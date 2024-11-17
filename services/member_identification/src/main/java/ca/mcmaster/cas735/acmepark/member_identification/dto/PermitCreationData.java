package ca.mcmaster.cas735.acmepark.member_identification.dto;

import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.Permit;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PermitCreationData {
    private String organizationId;
    private UserType userType;
    private List<String> licensePlates;
    private Boolean isRenew;

    public Permit asPermit() {
        Permit permit = new Permit();
        permit.setOrganizationId(organizationId);
        permit.setUserType(userType);
        permit.setLicensePlates(licensePlates);
        return permit;
    }
}
