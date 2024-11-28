package ca.mcmaster.cas735.acmepark.common.dtos;

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
