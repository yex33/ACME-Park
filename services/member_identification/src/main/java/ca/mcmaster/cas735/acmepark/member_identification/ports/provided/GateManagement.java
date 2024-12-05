package ca.mcmaster.cas735.acmepark.member_identification.ports.provided;

import ca.mcmaster.cas735.acmepark.member_identification.dto.AccessGateRequest;

public interface GateManagement {
    void requestGateOpen(AccessGateRequest request);
}
