package ca.mcmaster.cas735.acmepark.member_identification.ports.provided;

import ca.mcmaster.cas735.acmepark.member_identification.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.member_identification.dto.TransponderAccessData;

public interface TransponderManagement {
    void requestGateOpen(TransponderAccessData data)  throws NotFoundException;
    void issueTransponderByPermitId(String permitId);
}
