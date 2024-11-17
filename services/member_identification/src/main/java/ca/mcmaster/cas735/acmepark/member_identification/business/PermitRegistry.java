package ca.mcmaster.cas735.acmepark.member_identification.business;

import ca.mcmaster.cas735.acmepark.common.dtos.ParkingPermitInfo;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.Permit;
import ca.mcmaster.cas735.acmepark.member_identification.business.errors.AlreadyExistingException;
import ca.mcmaster.cas735.acmepark.member_identification.dto.PermitCreationData;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.PermitManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.required.PermitDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermitRegistry implements PermitManagement {
    private final PermitDataRepository database;

    @Autowired
    public PermitRegistry(PermitDataRepository database) {
        this.database = database;
    }

    @Override
    public void create(PermitCreationData request) throws AlreadyExistingException {
        Permit permit = request.asPermit();
        if (database.existsByOrganizationId(permit.getOrganizationId())) {
            throw new AlreadyExistingException("Permit", permit.getOrganizationId(), "organizationId");
        }

        return;
    }

    @Override
    public List<ParkingPermitInfo> findAll() {
        return database.findAll().stream().map(permit -> {
                    return new ParkingPermitInfo(
                            permit.getPermitId(),
                            permit.getOrganizationId(),
                            permit.getUserType(),
                            permit.getLicensePlates(),
                            permit.getTransponderId(),
                            permit.getStartDate(),
                            permit.getExpiryDate());
        }
        ).collect(Collectors.toList());
    }
}
