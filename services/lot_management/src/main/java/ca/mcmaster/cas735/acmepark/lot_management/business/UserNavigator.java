package ca.mcmaster.cas735.acmepark.lot_management.business;

import ca.mcmaster.cas735.acmepark.lot_management.business.entities.EntryRecord;
import ca.mcmaster.cas735.acmepark.lot_management.business.errors.RecordNotFoundException;
import ca.mcmaster.cas735.acmepark.lot_management.dtos.IssueUserFine;
import ca.mcmaster.cas735.acmepark.lot_management.dtos.IssueVehicleFine;
import ca.mcmaster.cas735.acmepark.lot_management.port.provided.IssueVehicleFineReceiver;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.EntryRecordDataRepository;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.IssueUserFineSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service @Slf4j
@AllArgsConstructor
public class UserNavigator implements IssueVehicleFineReceiver {
    private final EntryRecordDataRepository entryDb;
    private final IssueUserFineSender issueSender;

    @Override
    public void issueFine(IssueVehicleFine issueRequest) {
        String license = issueRequest.getLicensePlate();
        String userId = entryDb
                .findByLicensePlate(license)
                .map(EntryRecord::getEntryRecordId)
                .orElseThrow(() -> new RecordNotFoundException("Record Not Found"));

        log.info("Send fine to the user with ID: {}", userId);
        IssueUserFine issueUser = new IssueUserFine();
        issueUser.setUserID(userId);
        issueUser.setFine(issueRequest.getFine());
        issueSender.sendFine(issueUser);
        log.debug("Fine sent.");
    }
}
