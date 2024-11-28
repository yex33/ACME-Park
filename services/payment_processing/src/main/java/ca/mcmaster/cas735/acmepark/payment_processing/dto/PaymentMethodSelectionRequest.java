package ca.mcmaster.cas735.acmepark.payment_processing.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class PaymentMethodSelectionRequest {
    PaymentRequest invoice;
    Long invoiceId;
    List<PaymentMethod> paymentMethods;
}
