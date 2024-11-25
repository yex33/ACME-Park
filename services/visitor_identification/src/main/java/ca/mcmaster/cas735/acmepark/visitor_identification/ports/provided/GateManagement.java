package ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided;

import ca.mcmaster.cas735.acmepark.common.dtos.AccessGateRequest;

public interface GateManagement {
    void requestGateOpen(AccessGateRequest request);
}
