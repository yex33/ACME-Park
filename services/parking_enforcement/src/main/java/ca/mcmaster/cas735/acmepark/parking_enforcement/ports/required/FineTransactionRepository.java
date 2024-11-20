package ca.mcmaster.cas735.acmepark.parking_enforcement.ports.required;

import ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities.FineTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FineTransactionRepository extends JpaRepository<FineTransaction, String> {
}
