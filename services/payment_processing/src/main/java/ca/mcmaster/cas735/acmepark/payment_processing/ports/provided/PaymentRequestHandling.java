package ca.mcmaster.cas735.acmepark.payment_processing.ports.provided;

import ca.mcmaster.cas735.acmepark.payment_processing.dto.InvoiceDto;

public interface PaymentRequestHandling {
    void handlePaymentRequest(InvoiceDto invoiceDto);
}
