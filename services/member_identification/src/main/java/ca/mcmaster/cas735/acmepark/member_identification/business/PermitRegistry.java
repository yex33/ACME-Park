package ca.mcmaster.cas735.acmepark.member_identification.business;

import ca.mcmaster.cas735.acmepark.common.dtos.ParkingPermitInfo;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.MemberFeeTransaction;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.Permit;
import ca.mcmaster.cas735.acmepark.member_identification.business.errors.AlreadyExistingException;
import ca.mcmaster.cas735.acmepark.member_identification.dto.MemberFeeCreationData;
import ca.mcmaster.cas735.acmepark.member_identification.dto.PermitCreationData;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.MemberFeeManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.PermitManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.required.PermitDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermitRegistry implements PermitManagement {
    private final PermitDataRepository database;
    private final MemberFeeManagement feeManager;

    @Autowired
    public PermitRegistry(PermitDataRepository database, MemberFeeManagement feeManager) {
        this.database = database;
        this.feeManager = feeManager;
    }

    @Override
    public void create(PermitCreationData request) throws AlreadyExistingException {
        Permit permit = request.asPermit();
        if (database.existsByOrganizationId(permit.getOrganizationId())) {
            throw new AlreadyExistingException("Permit", permit.getOrganizationId(), "organizationId");
        }

        /* TODO:
        *   1. Create a transaction
        *   2. Pass the transaction to Parking Enforcement
        *   3. Listen to the payment succeed message
        *   4. Generate transponderId and create the permit row in database
        *   5. Mark the transaction as SUCCESS
        * */

        MemberFeeCreationData data = new MemberFeeCreationData(request.getOrganizationId(), request.getUserType(), 5000, LocalDateTime.now(), "");
        MemberFeeTransaction transaction = feeManager.createTransaction(data);

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
