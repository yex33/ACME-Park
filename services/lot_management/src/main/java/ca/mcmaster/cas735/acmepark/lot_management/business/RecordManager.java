package ca.mcmaster.cas735.acmepark.lot_management.business;

import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import ca.mcmaster.cas735.acmepark.lot_management.business.entities.EntryRecord;
import ca.mcmaster.cas735.acmepark.lot_management.business.errors.RecordNotFoundException;
import ca.mcmaster.cas735.acmepark.lot_management.business.policies.MaintainEntryRecord;
import ca.mcmaster.cas735.acmepark.lot_management.dtos.IssueUserFine;
import ca.mcmaster.cas735.acmepark.lot_management.dtos.IssueVehicleFine;
import ca.mcmaster.cas735.acmepark.lot_management.port.provided.IssueVehicleFineReceiver;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.IssueUserFineSender;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.RecordDataRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class RecordManager implements IssueVehicleFineReceiver, MaintainEntryRecord {
    private final RecordDataRepository database;
    private final IssueUserFineSender issue_sender;

    @Autowired
    public RecordManager(RecordDataRepository database, IssueUserFineSender issue_sender) {
        this.database = database;
        this.issue_sender = issue_sender;
    }

    @Override
    @Transactional
    public void insertRecord(String license, String userId, UserType userType) {
        if (database.findByLicensePlate(license).isPresent()) {
            throw new IllegalStateException("A record with the license plate '" + license + "' already exists.");
        }
        EntryRecord record = new EntryRecord();
        record.setLicensePlate(license);
        record.setUserId(userId);
        record.setUserType(userType);
        database.save(record);
        log.debug("Record inserted.");
    }

    @Override
    public void deleteRecord() {
        // exit
        // check license plate and their typ

    }

    @Override
    public EntryRecord findRecord(String license) {
        log.info("Looking up records by {}", license);
        return database.findByLicensePlate(license).orElseThrow(() -> new RecordNotFoundException("Record Not Found"));
    }

    @Override
    public void issueFine(IssueVehicleFine issueRequest) throws NotFoundException {
        String userId = findRecord(issueRequest.getLicensePlate()).getUserId();
        log.info("Send fine to the user with ID: {}", userId);
        IssueUserFine issueUser = new IssueUserFine();
        issueUser.setUserID(userId);
        issueUser.setFine(issueRequest.getFine());
        issue_sender.sendFine(issueUser);
        log.info("Fine sent!");
    }
}
