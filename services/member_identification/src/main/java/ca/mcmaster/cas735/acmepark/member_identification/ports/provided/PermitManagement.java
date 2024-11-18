package ca.mcmaster.cas735.acmepark.member_identification.ports.provided;

import ca.mcmaster.cas735.acmepark.common.dtos.ParkingPermitInfo;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.Permit;
import ca.mcmaster.cas735.acmepark.member_identification.business.errors.AlreadyExistingException;
import ca.mcmaster.cas735.acmepark.member_identification.dto.PermitCreationData;

import java.util.List;

public interface PermitManagement {
    void create(PermitCreationData request) throws AlreadyExistingException;
    List<ParkingPermitInfo> findAll();
}
