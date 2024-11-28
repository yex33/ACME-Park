package ca.mcmaster.cas735.acmepark.payment_processing.business.entities;

import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ChargeType {
    MEMBERSHIP(TransactionType.MEMBER_FEE),
    PAY_PER_USE(TransactionType.PARKING_FEE),
    FINE(TransactionType.VIOLATION_FINE);

    private final TransactionType transactionType;
}
