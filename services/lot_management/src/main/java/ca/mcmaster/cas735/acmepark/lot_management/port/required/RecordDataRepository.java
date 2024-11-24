package ca.mcmaster.cas735.acmepark.lot_management.port.required;

import ca.mcmaster.cas735.acmepark.lot_management.business.entities.EntryRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecordDataRepository extends JpaRepository<EntryRecord, String> {
    Optional<EntryRecord> findByLicensePlate(String license);
}
