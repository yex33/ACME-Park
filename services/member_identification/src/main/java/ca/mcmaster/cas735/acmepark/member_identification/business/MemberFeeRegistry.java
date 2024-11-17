package ca.mcmaster.cas735.acmepark.member_identification.business;

import ca.mcmaster.cas735.acmepark.common.dtos.TransactionStatus;
import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.MemberFeeTransaction;
import ca.mcmaster.cas735.acmepark.member_identification.dto.MemberFeeCreationData;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.MemberFeeManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.required.MemberFeeTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MemberFeeRegistry implements MemberFeeManagement {

    private final MemberFeeTransactionRepository database;

    @Autowired
    public MemberFeeRegistry(MemberFeeTransactionRepository database) {
        this.database = database;
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
        database.saveAndFlush(transaction);

        return transaction;
    }
}
