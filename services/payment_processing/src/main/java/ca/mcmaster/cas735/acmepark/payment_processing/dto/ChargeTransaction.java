package ca.mcmaster.cas735.acmepark.payment_processing.dto;

import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChargeTransaction {
    String transactionId;
    TransactionType transactionType;
}
