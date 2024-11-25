package ca.mcmaster.cas735.acmepark.common.dtos;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class ChargeDto {
    Long transactionId;
    ChargeType chargeType;
    String description;
    Integer amount;
    LocalDate issuedOn;
}
