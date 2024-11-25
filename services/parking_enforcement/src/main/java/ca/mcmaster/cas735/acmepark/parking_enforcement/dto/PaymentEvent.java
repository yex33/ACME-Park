package ca.mcmaster.cas735.acmepark.parking_enforcement.dto;

import lombok.Value;

import java.util.List;

@Value
public class PaymentEvent {
    PaymentStatus status;
    List<ChargeTransaction> transactions;
}
