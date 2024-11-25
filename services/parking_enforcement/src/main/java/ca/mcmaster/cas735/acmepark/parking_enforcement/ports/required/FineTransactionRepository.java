package ca.mcmaster.cas735.acmepark.parking_enforcement.ports.required;

import ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities.FineTransaction;
import ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FineTransactionRepository extends JpaRepository<FineTransaction, String> {
    Optional<FineTransaction> findById(Long id);
    List<FineTransaction> findByUserIdAndStatusIs(UUID userId, TransactionStatus status);
}
