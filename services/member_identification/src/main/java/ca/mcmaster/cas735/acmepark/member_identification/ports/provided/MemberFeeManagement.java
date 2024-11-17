package ca.mcmaster.cas735.acmepark.member_identification.ports.provided;

import ca.mcmaster.cas735.acmepark.member_identification.business.entities.MemberFeeTransaction;
import ca.mcmaster.cas735.acmepark.member_identification.dto.MemberFeeCreationData;

public interface MemberFeeManagement {
    MemberFeeTransaction createTransaction(MemberFeeCreationData request);
}
