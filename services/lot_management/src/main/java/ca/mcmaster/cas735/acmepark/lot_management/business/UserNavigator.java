package ca.mcmaster.cas735.acmepark.lot_management.business;

import ca.mcmaster.cas735.acmepark.lot_management.business.entities.EntryRecord;
import ca.mcmaster.cas735.acmepark.lot_management.dtos.IssueUserFine;
import ca.mcmaster.cas735.acmepark.lot_management.dtos.IssueVehicleFine;
import ca.mcmaster.cas735.acmepark.lot_management.port.provided.IssueVehicleFineReceiver;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.EntryRecordDataRepository;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.IssueUserFineSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service @Slf4j
@AllArgsConstructor
public class UserNavigator implements IssueVehicleFineReceiver {
    private final EntryRecordDataRepository entryDb;
    private final IssueUserFineSender issueSender;

    @Override
    public void issueFine(String license, IssueVehicleFine issueRequest) throws NoSuchElementException {
    String userId =
        entryDb
            .findByLicensePlate(license)
            .map(EntryRecord::getUserId)
            .orElseThrow(() -> new NoSuchElementException("License plate not found"));
        log.info("Issuing fine to user: {}, license: {}", userId, license);

        issueSender.sendFine(IssueUserFine.builder()
                        .amount(issueRequest.getAmount())
                        .userID(userId)
                        .description(issueRequest.getDescription())
                        .issuedOn(LocalDateTime.now())
                        .build());
        log.info("Fine successfully sent to user: {}, license: {}", userId, license);
    }
}
