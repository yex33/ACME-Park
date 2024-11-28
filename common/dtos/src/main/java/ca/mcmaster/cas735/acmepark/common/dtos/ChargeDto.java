package ca.mcmaster.cas735.acmepark.common.dtos;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Value
@Builder
@AllArgsConstructor
@Jacksonized
public class ChargeDto {
    String transactionId;
    TransactionType transactionType;

    // Description or purpose of the transaction
    String description;

    // The amount involved in the transaction, measured in cent, e.g. 2000 is equal to 20.00 dollar
    Integer amount;

    // The timestamp when the transaction was initiated
    LocalDate issuedOn;
}