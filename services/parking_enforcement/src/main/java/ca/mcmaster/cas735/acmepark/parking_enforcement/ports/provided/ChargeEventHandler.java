package ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided;

import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.InvoiceDto;

public interface ChargeEventHandler {
    InvoiceDto attachFines(InvoiceDto invoiceDto);
}
