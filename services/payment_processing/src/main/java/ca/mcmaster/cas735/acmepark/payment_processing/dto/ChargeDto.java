package ca.mcmaster.cas735.acmepark.payment_processing.dto;

import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class ChargeDto {
    String transactionId;
    TransactionType transactionType;
    String description;
    Integer amount;
    LocalDate issuedOn;
}
