package ca.mcmaster.cas735.acmepark.member_identification.business;

import ca.mcmaster.cas735.acmepark.common.dtos.ParkingPermitInfo;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.MemberFeeTransaction;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.Permit;
import ca.mcmaster.cas735.acmepark.member_identification.business.errors.AlreadyExistingException;
import ca.mcmaster.cas735.acmepark.member_identification.dto.MemberFeeCreationData;
import ca.mcmaster.cas735.acmepark.member_identification.dto.PermitCreationData;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.*;
import ca.mcmaster.cas735.acmepark.member_identification.ports.required.PermitDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PermitRegistry implements PermitManagement {
    private final PermitDataRepository database;
    private final MemberFeeManagement feeManager;
    private final PaymentSender paymentManager;

    @Autowired
    public PermitRegistry(PermitDataRepository database, MemberFeeManagement feeManager, PaymentSender paymentManager) {
        this.database = database;
        this.feeManager = feeManager;
        this.paymentManager = paymentManager;
    }

    @Override
    public void create(PermitCreationData request) throws AlreadyExistingException {
        log.info("Starting the creation process for a new permit with data: {}", request);

        Permit permit = request.asPermit();
        permit.setPermitId(UUID.randomUUID().toString());
        permit.setProcessed(false);

        log.info("Generated permit ID: {}", permit.getPermitId());

        if (database.existsByOrganizationId(permit.getOrganizationId()) && !request.getIsRenew()) {
            log.warn("Permit creation failed. Organization ID {} already has an existing permit.", permit.getOrganizationId());
            throw new AlreadyExistingException("Permit", permit.getOrganizationId(), "organizationId");
        }

        database.saveAndFlush(permit);
        log.info("Permit saved to the database: {}", permit);

        /* TODO:
         *   1. Create a transaction
         *   2. Pass the transaction to Parking Enforcement
         *   3. Listen to the payment succeed message
         *   4. Generate transponderId and create the permit row in database
         *   5. Mark the transaction as SUCCESS
         * */

        int amount = 0;

        // Assign the permit fee based on user type and log the calculated fee
        switch (permit.getUserType()) {
            case STUDENT -> {
                amount = 30000; // $300.00
                log.info("User type is STUDENT. Discounted fee applied: {}", amount);
            }
            case STAFF, FACULTY -> {
                amount = 80000; // $800.00
                log.info("User type is {}. Standard fee applied: {}", permit.getUserType(), amount);
            }
            default -> log.warn("Unexpected user type: {}", permit.getUserType());
        }

        MemberFeeCreationData data = new MemberFeeCreationData(request.getOrganizationId(), request.getUserType(), amount, LocalDateTime.now(), "", permit.getPermitId());
        log.info("Member fee data prepared for transaction: {}", data);

        MemberFeeTransaction transaction = feeManager.createTransaction(data);
        log.info("Transaction created successfully: {}", transaction);

        paymentManager.sendTransaction(transaction);
        log.info("Transaction sent to payment manager for processing: {}", transaction);

        log.info("Permit creation process completed for permit ID: {}", permit.getPermitId());
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
