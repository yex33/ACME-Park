package ca.mcmaster.cas735.acmepark.lot_management.port.provided;

import ca.mcmaster.cas735.acmepark.common.dtos.AccessGateRequest;

public interface AccessGateRequestReceiver {
    void checkRule(AccessGateRequest accessRequest);
}