package ca.mcmaster.cas735.acmepark.parking_enforcement.dto;

import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import lombok.Value;

@Value
public class ChargeTransaction {
    Long transactionId;
    TransactionType transactionType;
}
