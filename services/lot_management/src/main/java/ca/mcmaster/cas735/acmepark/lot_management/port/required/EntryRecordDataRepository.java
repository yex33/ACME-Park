package ca.mcmaster.cas735.acmepark.lot_management.port.required;

import ca.mcmaster.cas735.acmepark.lot_management.business.entities.EntryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntryRecordDataRepository extends JpaRepository<EntryRecord, String> {
    Optional<EntryRecord> findByLicensePlate(String license);
    Optional<EntryRecord> findByLicensePlateAndGateIdAndExitRecord_ExitTimeIsNull(
            String licensePlate, String gateId);
}
