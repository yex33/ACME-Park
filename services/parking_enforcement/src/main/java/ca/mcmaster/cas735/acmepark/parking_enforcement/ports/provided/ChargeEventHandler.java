package ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided;

import ca.mcmaster.cas735.acmepark.common.dtos.PaymentRequest;

public interface ChargeEventHandler {
    PaymentRequest attachFines(PaymentRequest paymentRequest);
}
