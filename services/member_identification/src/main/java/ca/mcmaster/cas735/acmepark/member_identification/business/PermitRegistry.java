package ca.mcmaster.cas735.acmepark.member_identification.business;

import ca.mcmaster.cas735.acmepark.common.dtos.ParkingPermitInfo;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.MemberFeeTransaction;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.Permit;
import ca.mcmaster.cas735.acmepark.member_identification.business.errors.AlreadyExistingException;
import ca.mcmaster.cas735.acmepark.member_identification.dto.MemberFeeCreationData;
import ca.mcmaster.cas735.acmepark.member_identification.dto.PermitCreationData;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.MemberFeeManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.PaymentManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.PermitManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.TransponderManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.required.PermitDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PermitRegistry implements PermitManagement, TransponderManagement {
    private final PermitDataRepository database;
    private final MemberFeeManagement feeManager;
    private final PaymentManagement paymentManager;

    @Autowired
    public PermitRegistry(PermitDataRepository database, MemberFeeManagement feeManager, PaymentManagement paymentManager) {
        this.database = database;
        this.feeManager = feeManager;
        this.paymentManager = paymentManager;
    }

    @Override
    public void create(PermitCreationData request) throws AlreadyExistingException {
        Permit permit = request.asPermit();
        permit.setPermitId(UUID.randomUUID().toString());
        permit.setProcessed(false);

        if (database.existsByOrganizationId(permit.getOrganizationId()) && !request.getIsRenew()) {
            throw new AlreadyExistingException("Permit", permit.getOrganizationId(), "organizationId");
        }

        database.saveAndFlush(permit);

        /* TODO:
        *   1. Create a transaction
        *   2. Pass the transaction to Parking Enforcement
        *   3. Listen to the payment succeed message
        *   4. Generate transponderId and create the permit row in database
        *   5. Mark the transaction as SUCCESS
        * */

        int amount = 0;

        // Give students some discount, they are not making money
        switch (permit.getUserType()) {
            case STUDENT -> {
                amount = 30000; // $300.00
            }
            case STAFF, FACULTY -> {
                amount = 80000; // $800.00
            }
        }

        MemberFeeCreationData data = new MemberFeeCreationData(request.getOrganizationId(), request.getUserType(), amount, LocalDateTime.now(), "", permit.getPermitId());
        MemberFeeTransaction transaction = feeManager.createTransaction(data);

        paymentManager.sendTransaction(transaction);

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

    @Override
    public void requestGateOpen(String transponderId) {
        Permit permit = database.findPermitByTransponderId(transponderId);

        if (permit == null) {
            return;
        }

        if (permit.isExpired()) {

        } else {

        }
    }

    @Override
    public void issueTransponderByPermitId(String permitId) {
        Permit permit = database.findPermitByPermitId(permitId);

        permit.setStartDate(LocalDate.now());
        permit.setExpiryDate(LocalDate.now().plusYears(1));
        permit.setTransponderId(UUID.randomUUID().toString());
        permit.setProcessed(true);

        database.updatePermitByPermitId(permitId, permit);
    }


}
