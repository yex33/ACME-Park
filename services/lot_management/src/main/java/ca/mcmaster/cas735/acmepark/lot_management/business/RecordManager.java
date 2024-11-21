package ca.mcmaster.cas735.acmepark.lot_management.business;

import ca.mcmaster.cas735.acmepark.lot_management.business.entities.EntryRecords;
import ca.mcmaster.cas735.acmepark.lot_management.business.errors.RecordNotFoundException;
import ca.mcmaster.cas735.acmepark.lot_management.dtos.IssueUserFine;
import ca.mcmaster.cas735.acmepark.lot_management.port.provided.LookupUser;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.IssueUserFineSender;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.RecordDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class RecordManager implements LookupUser {
    private final RecordDataRepository database;
    private final IssueUserFineSender issue_sender;

    @Autowired
    public RecordManager(RecordDataRepository database, IssueUserFineSender issue_sender) {
        this.database = database;
        this.issue_sender = issue_sender;
    }

    @Override
    public void findRecord(String license, Integer fine) {
        log.info("Looking up users by {}", license);
        Integer userId = database.findByLicensePlate(license)
                                .map(EntryRecords::getUserId)
                                .orElseThrow(() -> new RecordNotFoundException("Record not found"));

        log.info("Send fine to the user with ID: {}", userId);
        IssueUserFine issueUser = new IssueUserFine();
        issueUser.setUserID(userId);
        issueUser.setFine(fine);
        issue_sender.sendApproval(issueUser);
        log.info("Fine sent!");
    }
}
