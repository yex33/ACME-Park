package ca.mcmaster.cas735.acmepark.payment_processing.ports.provided;

import ca.mcmaster.cas735.acmepark.payment_processing.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.payment_processing.dto.PaymentEvent;
import ca.mcmaster.cas735.acmepark.payment_processing.dto.PaymentMethodSelection;
import ca.mcmaster.cas735.acmepark.payment_processing.dto.PaymentMethodSelectionRequest;

public interface PaymentRequestHandling {
    PaymentMethodSelectionRequest attachAvailablePaymentMethods(PaymentRequest paymentRequest);
    PaymentEvent processPayment(PaymentMethodSelection paymentMethodSelection);
}
