package ca.mcmaster.cas735.acmepark.member_identification.business;

import ca.mcmaster.cas735.acmepark.common.dtos.TransactionStatus;
import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.MemberFeeTransaction;
import ca.mcmaster.cas735.acmepark.member_identification.dto.MemberFeeCreationData;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.MemberFeeManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.MonitorDataSender;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.TransponderManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.required.MemberFeeTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
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
        database.saveAndFlush(transaction);

        return transaction;
    }

    @Override
    public void completeTransaction(String transactionId) {
        MemberFeeTransaction transaction = database.findByTransactionId(transactionId);

        if (transaction == null) { return; }

        transaction.setTransactionStatus(TransactionStatus.SUCCESS);

        database.saveAndFlush(transaction);

        transponderManager.issueTransponderByPermitId(transaction.getAssociatedPermitId());

        monitorDataSender.sendPermitSale(transaction.getAssociatedPermitId());
    }


}
