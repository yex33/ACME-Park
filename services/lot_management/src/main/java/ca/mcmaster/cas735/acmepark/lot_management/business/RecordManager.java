package ca.mcmaster.cas735.acmepark.lot_management.business;

import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import ca.mcmaster.cas735.acmepark.lot_management.business.entities.EntryRecord;
import ca.mcmaster.cas735.acmepark.lot_management.business.entities.ExitRecord;
import ca.mcmaster.cas735.acmepark.lot_management.business.errors.RecordNotFoundException;
import ca.mcmaster.cas735.acmepark.lot_management.business.internal.MaintainRecord;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service @Slf4j
@AllArgsConstructor
public class RecordManager implements MaintainRecord {
    private final EntryRecordDataRepository entryDb;
    private final ExitRecordDataRepository exitDb;

    @Override
    @Transactional
    public void insertRecord(String license, String userId, UserType userType, String gateId) {
        EntryRecord entryRecord = EntryRecord.builder()
                .userId(userId)
                .licensePlate(license)
                .userType(userType)
                .gateId(gateId)
                .entryTime(LocalDateTime.now())
                .build();
        entryDb.save(entryRecord);
        log.debug("New entry record inserted.");

        exitDb.save(ExitRecord.builder()
                        .entryRecord(entryRecord)
                        .licensePlate(license)
                        .userId(userId)
                        .userType(userType)
                        .gateId(gateId)
                        .build());
        log.debug("New exit record inserted");
    }

    @Override
    public void updateExitRecord(String gateId, String license) {
        EntryRecord entryRecord = entryDb
                .findByLicensePlateAndGateIdAndExitRecord_ExitTimeIsNull(license, gateId)
                .orElseThrow(() -> new RecordNotFoundException("No active entry record found for license plate: " + license));
        log.debug("Entry record found: {}", entryRecord.getEntryRecordId());

        ExitRecord exitRecord = entryRecord.getExitRecord();
        exitRecord.setExitTime(LocalDateTime.now());
        exitDb.save(exitRecord);
        log.debug("Exit record updated for vehicle: {}", license);
    }
}
