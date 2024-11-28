package ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided;

public interface ExitLot {
    void exitGateOpen(String gateId, String license);
}
