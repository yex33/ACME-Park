package ca.mcmaster.cas735.acmepark.visitor_identification.business;

import ca.mcmaster.cas735.acmepark.common.dtos.TransactionStatus;
import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import ca.mcmaster.cas735.acmepark.visitor_identification.business.entities.ParkingFeeTransaction;
import ca.mcmaster.cas735.acmepark.visitor_identification.dto.ParkingFeeCreationData;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.ExitLot;
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
    private final ExitLot gateOpener;

    @Autowired
    public ParkingFeeTransactionRegistry(ParkingFeeTransactionRepository database, PaymentSender paymentManager, ExitLot gateOpener) {
        this.database = database;
        this.paymentManager = paymentManager;
        this.gateOpener = gateOpener;
    }

    @Override
    public void issueParkingFee(ParkingFeeCreationData feeData) {
        ParkingFeeTransaction transaction = new ParkingFeeTransaction();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setAmount(feeData.getAmount());
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transaction.setInitiatedBy(feeData.getVisitorId());
        transaction.setGateId(feeData.getGateId());
        transaction.setLicensePlate(feeData.getLicensePlate());
        transaction.setTimestamp(feeData.getTimestamp());
        transaction.setUserType(UserType.VISITOR);
        transaction.setTransactionType(TransactionType.PARKING_FEE);

        database.saveAndFlush(transaction);

        paymentManager.sendTransaction(transaction);
    }

    @Override
    public void handleParkingFeeStatusChanged(String transactionId) {
        database.findByTransactionId(transactionId).ifPresent(transaction -> {
            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
            database.saveAndFlush(transaction);

            gateOpener.exitGateOpen(transaction.getGateId(), transaction.getLicensePlate());
        });
    }
}
