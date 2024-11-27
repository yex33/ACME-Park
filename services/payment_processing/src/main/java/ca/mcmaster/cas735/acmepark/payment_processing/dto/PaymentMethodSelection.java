package ca.mcmaster.cas735.acmepark.payment_processing.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PaymentMethodSelection {
    Long invoiceId;
    PaymentMethod paymentMethod;
}
