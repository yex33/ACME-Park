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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
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
        log.info("Issuing parking fee with data: {}", feeData);

        // Create a new parking fee transaction
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

        log.info("Created parking fee transaction: {}", transaction);

        // Save transaction to the database
        database.saveAndFlush(transaction);
        log.info("Parking fee transaction saved to the database with ID: {}", transaction.getTransactionId());

        // Send the transaction to the payment manager
        paymentManager.sendTransaction(transaction);
        log.info("Payment transaction sent for parking fee with ID: {}", transaction.getTransactionId());
    }

    @Override
    public void handleParkingFeeStatusChanged(String transactionId) {
        log.info("Handling parking fee status change for transaction ID: {}", transactionId);

        database.findByTransactionId(transactionId).ifPresentOrElse(transaction -> {
            log.info("Transaction found: {}", transaction);

            // Update the transaction status to SUCCESS
            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
            database.saveAndFlush(transaction);
            log.info("Transaction status updated to SUCCESS for ID: {}", transactionId);

            // Trigger gate open for exit
            gateOpener.exitGateOpen(transaction.getGateId(), transaction.getLicensePlate());
            log.info("Exit gate open triggered for gate ID: {} and license plate: {}", transaction.getGateId(), transaction.getLicensePlate());
        }, () -> {
            log.warn("Transaction with ID: {} not found. No status change handled.", transactionId);
        });
    }
}
