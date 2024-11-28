package ca.mcmaster.cas735.acmepark.parking_enforcement.dto;

import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ChargeTransaction {
    Long transactionId;
    TransactionType transactionType;
}
