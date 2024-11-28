package ca.mcmaster.cas735.acmepark.member_identification.ports.required;

import ca.mcmaster.cas735.acmepark.member_identification.business.entities.MemberFeeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberFeeTransactionRepository extends JpaRepository<MemberFeeTransaction, String> {
    Optional<MemberFeeTransaction> findByTransactionId(String transactionId);
}
