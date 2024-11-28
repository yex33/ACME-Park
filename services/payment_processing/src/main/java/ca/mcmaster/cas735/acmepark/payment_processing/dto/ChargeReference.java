package ca.mcmaster.cas735.acmepark.payment_processing.dto;

import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ChargeReference {
    String transactionId;
    TransactionType transactionType;
}
