package ca.mcmaster.cas735.acmepark.member_identification.ports.provided;

public interface TransponderManagement {
    void requestGateOpen(String transponderId);
    void issueTransponderByPermitId(String permitId);
}
