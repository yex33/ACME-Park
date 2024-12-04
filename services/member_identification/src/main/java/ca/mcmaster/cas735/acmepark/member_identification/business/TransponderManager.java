package ca.mcmaster.cas735.acmepark.member_identification.business;

import ca.mcmaster.cas735.acmepark.common.dtos.AccessGateRequest;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.Permit;
import ca.mcmaster.cas735.acmepark.member_identification.business.errors.AlreadyExistingException;
import ca.mcmaster.cas735.acmepark.member_identification.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.member_identification.dto.TransponderAccessData;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.GateManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.TransponderManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.TransponderSender;
import ca.mcmaster.cas735.acmepark.member_identification.ports.required.PermitDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
public class TransponderManager implements TransponderManagement {

    private final PermitDataRepository database;
    private final GateManagement gateManager;
    private final TransponderSender transponderSender;

    @Autowired
    public TransponderManager(PermitDataRepository database, GateManagement gateManager, TransponderSender transponderSender) {
        this.database = database;
        this.gateManager = gateManager;
        this.transponderSender = transponderSender;
    }

    @Override
    public void requestGateOpen(TransponderAccessData data) throws NotFoundException {
        log.info("Received request to open gate with transponder data: {}", data);

        Permit permit = database.findPermitByTransponderId(data.getTransponderId());
        if (permit == null) {
            log.warn("No permit found for transponder ID: {}", data.getTransponderId());
            throw new NotFoundException("Transponder", data.getTransponderId(), "transponderId");
        }

        if (permit.isExpired()) {
            log.warn("Permit with ID: {} has expired. Transponder access denied.", permit.getPermitId());
            throw new NotFoundException("Transponder", data.getTransponderId(), "transponderId");
        }

        log.info("Permit valid for transponder ID: {}. Proceeding with gate access.", data.getTransponderId());

        AccessGateRequest request = new AccessGateRequest();
        request.setGateId(data.getGateId());
        request.setLicense(data.getLicensePlate());
        request.setUserId(permit.getOrganizationId());
        request.setUserType(permit.getUserType());

        log.info("Prepared gate access request: {}", request);

        gateManager.requestGateOpen(request);
        log.info("Gate access request sent successfully for transponder ID: {}", data.getTransponderId());
    }

    @Override
    public void issueTransponderByPermitId(String permitId) {
        log.info("Issuing transponder for permit ID: {}", permitId);

        Permit permit = database.findPermitByPermitId(permitId);
        if (permit == null) {
            log.error("No permit found for permit ID: {}. Transponder issuance failed.", permitId);
            return;
        }

        permit.setStartDate(LocalDate.now());
        permit.setExpiryDate(LocalDate.now().plusYears(1));
        permit.setTransponderId(UUID.randomUUID().toString());
        permit.setProcessed(true);

        log.info("Updated permit details for transponder issuance: {}", permit);

        database.saveAndFlush(permit);
        log.info("Permit with ID: {} updated and transponder issued successfully.", permitId);

        this.transponderSender.sendTransponder(permit.getTransponderId());
    }
}
