package ca.mcmaster.cas735.acmepark.lot_management.port.required;

import ca.mcmaster.cas735.acmepark.lot_management.business.entities.ExitRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExitRecordDataRepository extends JpaRepository<ExitRecord, String> {
    List<ExitRecord> findByExitTimeIsNullAndGateId(String gateId);
}
