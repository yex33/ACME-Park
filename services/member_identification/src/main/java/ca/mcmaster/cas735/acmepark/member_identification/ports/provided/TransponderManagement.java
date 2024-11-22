package ca.mcmaster.cas735.acmepark.member_identification.ports.provided;

import ca.mcmaster.cas735.acmepark.member_identification.dto.TransponderAccessData;

public interface TransponderManagement {
    void requestGateOpen(TransponderAccessData data);
    void issueTransponderByPermitId(String permitId);
}
