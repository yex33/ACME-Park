package ca.mcmaster.cas735.acmepark.lot_management.business.internal;

import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import org.springframework.stereotype.Component;

@Component(value = "maintainer")
public interface MaintainRecord {
    void insertRecord(String license, String userId, UserType userType, String gateId);
    void updateExitRecord(String gateId, String license);
}
