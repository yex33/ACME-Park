package ca.mcmaster.cas735.acmepark.lot_management.port.provided;


import ca.mcmaster.cas735.acmepark.lot_management.dtos.AccessGateRequest;

public interface AccessGateRequestReceiver {
    void checkRule(AccessGateRequest accessRequest);
}
