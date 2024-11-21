package ca.mcmaster.cas735.acmepark.lot_management.business.entities;

import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor
public class AccessRule {
    private String gatID;
    private List<UserType> accessUsers;
}
