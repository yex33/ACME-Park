package ca.mcmaster.cas735.acmepark.member_identification.ports.required;

import ca.mcmaster.cas735.acmepark.member_identification.business.entities.MemberFeeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberFeeTransactionRepository extends JpaRepository<MemberFeeTransaction, String> {
    MemberFeeTransaction findByTransactionId(String transactionId);
    void updateMemberFeeTransactionByTransactionId(String transactionId, MemberFeeTransaction memberFeeTransaction);
}
