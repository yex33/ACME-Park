package ca.mcmaster.cas735.acmepark.lot_management.business.policies;

import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import ca.mcmaster.cas735.acmepark.lot_management.business.entities.EntryRecord;
import org.springframework.stereotype.Component;

@Component(value = "maintainer")
public interface MaintainEntryRecord {
    void insertRecord(String license, String userId, UserType userType);
    void deleteRecord();
    EntryRecord findRecord(String license);
}
