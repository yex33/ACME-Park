package ca.mcmaster.cas735.acmepark.payment_processing.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class PaymentMethodSelectionRequest {
    InvoiceDto invoice;
    Long invoiceId;
    List<PaymentMethod> paymentMethods;
}
