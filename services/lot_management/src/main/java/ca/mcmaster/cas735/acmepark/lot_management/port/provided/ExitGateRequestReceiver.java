package ca.mcmaster.cas735.acmepark.lot_management.port.provided;

import ca.mcmaster.cas735.acmepark.common.dtos.ExitGateRequest;

public interface ExitGateRequestReceiver {
    void allowExit(ExitGateRequest exitGateRequest);
}
