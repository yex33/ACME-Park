package ca.mcmaster.cas735.acmepark.lot_management.port.required;

import ca.mcmaster.cas735.acmepark.lot_management.business.entities.EntryRecords;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecordDataRepository extends JpaRepository<EntryRecords, Integer> {
    Optional<EntryRecords> findByLicensePlate(String license);
}
