package ca.mcmaster.cas735.acmepark.member_identification.ports.provided;

import ca.mcmaster.cas735.acmepark.member_identification.business.entities.MemberFeeTransaction;

public interface PaymentManagement {
    void sendTransaction(MemberFeeTransaction transaction);
}
