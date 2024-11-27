package ca.mcmaster.cas735.acmepark.lot_management.port.required;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.ControlGate;

public interface ControlGateSender {
    void sendControlResult(ControlGate controlResult);
}
