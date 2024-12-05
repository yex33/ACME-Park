package ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided;

import ca.mcmaster.cas735.acmepark.visitor_identification.dto.AccessGateRequest;

public interface GateOpener {
    void requestGateOpen(AccessGateRequest request);
}
