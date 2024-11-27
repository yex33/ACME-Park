package ca.mcmaster.cas735.acmepark.payment_processing.ports.provided;

import ca.mcmaster.cas735.acmepark.payment_processing.dto.InvoiceDto;
import ca.mcmaster.cas735.acmepark.payment_processing.dto.PaymentMethodSelectionRequest;

public interface PaymentRequestHandling {
    PaymentMethodSelectionRequest attachAvailablePaymentMethods(InvoiceDto invoiceDto);
}
