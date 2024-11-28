package ca.mcmaster.cas735.acmepark.member_identification.business;

import ca.mcmaster.cas735.acmepark.common.dtos.TransactionStatus;
import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.MemberFeeTransaction;
import ca.mcmaster.cas735.acmepark.member_identification.dto.MemberFeeCreationData;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.MemberFeeManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.MonitorDataSender;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.TransponderManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.required.MemberFeeTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class MemberFeeRegistry implements MemberFeeManagement {

    private final MemberFeeTransactionRepository database;
    private final TransponderManagement transponderManager;
    private final MonitorDataSender monitorDataSender;

    @Autowired
    public MemberFeeRegistry(MemberFeeTransactionRepository database, TransponderManagement transponderManager, MonitorDataSender monitorDataSender) {
        this.database = database;
        this.transponderManager = transponderManager;
        this.monitorDataSender = monitorDataSender;
    }

    @Override
    public MemberFeeTransaction createTransaction(MemberFeeCreationData request) {
        log.info("Creating a new member fee transaction with request data: {}", request);

        MemberFeeTransaction transaction = new MemberFeeTransaction();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setTransactionType(TransactionType.MEMBER_FEE);
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transaction.setAmount(request.getAmount());
        transaction.setUserType(request.getUserType());
        transaction.setInitiatedBy(request.getOrganizationId());
        transaction.setTimestamp(request.getTimestamp());
        transaction.setDescription(request.getDescription());
        transaction.setAssociatedPermitId(request.getAssociatedPermitId());

        log.info("Generated transaction details: {}", transaction);

        database.saveAndFlush(transaction);
        log.info("Transaction saved successfully with ID: {}", transaction.getTransactionId());

        return transaction;
    }

    @Override
    public void completeTransaction(String transactionId) {
        log.info("Completing transaction with ID: {}", transactionId);

        database.findByTransactionId(transactionId).ifPresent(transaction -> {

            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
            database.saveAndFlush(transaction);
            log.info("Transaction status updated to SUCCESS for ID: {}", transactionId);

            transponderManager.issueTransponderByPermitId(transaction.getAssociatedPermitId());
            log.info("Issued transponder for permit ID: {}", transaction.getAssociatedPermitId());

            monitorDataSender.sendPermitSale(transaction.getAssociatedPermitId());
        });
    }
}
