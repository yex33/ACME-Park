package ca.mcmaster.cas735.acmepark.member_identification.business;

import ca.mcmaster.cas735.acmepark.common.dtos.ParkingPermitInfo;
import ca.mcmaster.cas735.acmepark.common.dtos.TransactionStatus;
import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.MemberFeeTransaction;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.Permit;
import ca.mcmaster.cas735.acmepark.member_identification.dto.MemberFeeCreationData;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.MemberFeeManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.PermitManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.TransponderManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.required.MemberFeeTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MemberFeeRegistry implements MemberFeeManagement {

    private final MemberFeeTransactionRepository database;
    private final TransponderManagement transponderManager;

    @Autowired
    public MemberFeeRegistry(MemberFeeTransactionRepository database, TransponderManagement transponderManager) {
        this.database = database;
        this.transponderManager = transponderManager;
    }

    @Override
    public MemberFeeTransaction createTransaction(MemberFeeCreationData request) {
        MemberFeeTransaction transaction = new MemberFeeTransaction();
        transaction.transactionId = UUID.randomUUID().toString();
        transaction.transactionType = TransactionType.MEMBER_FEE;
        transaction.transactionStatus = TransactionStatus.PENDING;
        transaction.amount = request.getAmount();
        transaction.userType = request.getUserType();
        transaction.initiatedBy = request.getOrganizationId();
        transaction.timestamp = request.getTimestamp();
        transaction.description = request.getDescription();
        transaction.setAssociatedPermitId(request.getAssociatedPermitId());
        database.saveAndFlush(transaction);

        return transaction;
    }

    @Override
    public void completeTransaction(String transactionId) {
        MemberFeeTransaction transaction = database.findByTransactionId(transactionId);

        if (transaction == null) { return; }

        transaction.transactionStatus = TransactionStatus.SUCCESS;

        database.updateMemberFeeTransactionByTransactionId(transactionId, transaction);

        transponderManager.issueTransponderByPermitId(transaction.getAssociatedPermitId());
    }


}
