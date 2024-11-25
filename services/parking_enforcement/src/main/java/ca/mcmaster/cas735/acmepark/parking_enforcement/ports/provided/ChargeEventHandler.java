package ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided;

import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.Invoice;

public interface ChargeEventHandler {
    Invoice attachFines(Invoice invoice);
}
