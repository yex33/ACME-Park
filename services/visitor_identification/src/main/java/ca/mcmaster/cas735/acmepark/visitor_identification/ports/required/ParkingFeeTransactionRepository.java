package ca.mcmaster.cas735.acmepark.visitor_identification.ports.required;

import ca.mcmaster.cas735.acmepark.visitor_identification.business.entities.ParkingFeeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingFeeTransactionRepository extends JpaRepository<ParkingFeeTransaction, String> {
    Optional<ParkingFeeTransaction> findByTransactionId(String transactionId);
}
