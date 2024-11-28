package ca.mcmaster.cas735.acmepark.member_identification.business;

import ca.mcmaster.cas735.acmepark.common.dtos.AccessGateRequest;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.Permit;
import ca.mcmaster.cas735.acmepark.member_identification.dto.TransponderAccessData;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.GateManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.TransponderManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.required.PermitDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransponderManager implements TransponderManagement {

    private final PermitDataRepository database;
    private final GateManagement gateManager;

    @Autowired
    public TransponderManager(PermitDataRepository database, GateManagement gateManager) {
        this.database = database;
        this.gateManager = gateManager;
    }

    @Override
    public void requestGateOpen(TransponderAccessData data) {
        Permit permit = database.findPermitByTransponderId(data.getTransponderId());

        if (permit == null) {
            return;
        }

        if (permit.isExpired()) {
            return;
        } else {
            AccessGateRequest request = new AccessGateRequest();
            request.setGateId(data.getGateId());
            request.setLicense(data.getLicensePlate());
            request.setUserId(permit.getOrganizationId());
            request.setUserType(permit.getUserType());
            gateManager.requestGateOpen(request);
        }
    }

    @Override
    public void issueTransponderByPermitId(String permitId) {
        Permit permit = database.findPermitByPermitId(permitId);

        permit.setStartDate(LocalDate.now());
        permit.setExpiryDate(LocalDate.now().plusYears(1));
        permit.setTransponderId(UUID.randomUUID().toString());
        permit.setProcessed(true);

        database.saveAndFlush(permit);
    }

}
