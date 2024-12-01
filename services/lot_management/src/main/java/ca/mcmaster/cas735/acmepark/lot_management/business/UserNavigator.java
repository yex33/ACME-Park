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
        try {
            String userId = entryDb.findByLicensePlate(license)
                    .map(EntryRecord::getUserId)
                    .orElseThrow(() -> new RecordNotFoundException("Record Not Found"));
            log.info("Issuing fine to user: {}, license: {}", userId, license);

            IssueUserFine issueUser = new IssueUserFine();
            issueUser.setUserID(userId);
            issueUser.setFine(issueRequest.getFine());
            issueSender.sendFine(issueUser);
            log.info("Fine successfully sent to user: {}, license: {}", userId, license);
        } catch (RecordNotFoundException e) {
            log.warn("Failed to issue fine: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while issuing fine for license: {}: {}", license, e.getMessage(), e);
        }
    }
}
