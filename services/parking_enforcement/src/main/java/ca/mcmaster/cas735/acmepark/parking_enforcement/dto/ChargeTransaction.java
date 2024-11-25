package ca.mcmaster.cas735.acmepark.parking_enforcement.dto;

import lombok.Value;

@Value
public class ChargeTransaction {
    Long transactionId;
    ChargeType chargeType;
}
