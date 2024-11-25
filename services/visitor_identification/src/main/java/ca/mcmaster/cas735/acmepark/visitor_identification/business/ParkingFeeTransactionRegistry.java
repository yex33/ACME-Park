package ca.mcmaster.cas735.acmepark.visitor_identification.business;

import ca.mcmaster.cas735.acmepark.common.dtos.TransactionStatus;
import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import ca.mcmaster.cas735.acmepark.visitor_identification.business.entities.ParkingFeeTransaction;
import ca.mcmaster.cas735.acmepark.visitor_identification.dto.ParkingFeeCreationData;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.ParkingFeeManagement;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.PaymentSender;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.required.ParkingFeeTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ParkingFeeTransactionRegistry implements ParkingFeeManagement {

    private final ParkingFeeTransactionRepository database;
    private final PaymentSender paymentManager;

    @Autowired
    public ParkingFeeTransactionRegistry(ParkingFeeTransactionRepository database, PaymentSender paymentManager) {
        this.database = database;
        this.paymentManager = paymentManager;
    }

    @Override
    public void issueParkingFee(ParkingFeeCreationData feeData) {
        ParkingFeeTransaction transaction = new ParkingFeeTransaction();
        transaction.transactionId = UUID.randomUUID().toString();
        transaction.amount = feeData.getAmount();
        transaction.transactionStatus = TransactionStatus.PENDING;
        transaction.initiatedBy = feeData.getVisitorId();
        transaction.timestamp = feeData.getTimestamp();
        transaction.userType = UserType.VISITOR;
        transaction.transactionType = TransactionType.PARKING_FEE;

        database.saveAndFlush(transaction);

        paymentManager.sendTransaction(transaction);
    }

    @Override
    public void parkingFeeStatusChanged() {

    }
}
