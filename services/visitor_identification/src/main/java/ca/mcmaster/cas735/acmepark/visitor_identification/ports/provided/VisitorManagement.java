package ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided;

public interface VisitorManagement {
    void requestAccess(String licensePlate, String gateId);
    void exit(String visitorId, String licensePlate, String voucherId);
}
