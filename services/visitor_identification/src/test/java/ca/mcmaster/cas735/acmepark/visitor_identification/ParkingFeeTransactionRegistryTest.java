package ca.mcmaster.cas735.acmepark.visitor_identification;

import ca.mcmaster.cas735.acmepark.common.dtos.TransactionStatus;
import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import ca.mcmaster.cas735.acmepark.visitor_identification.business.entities.ParkingFeeTransaction;
import ca.mcmaster.cas735.acmepark.visitor_identification.dto.ParkingFeeCreationData;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.ExitLot;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.PaymentSender;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.required.ParkingFeeTransactionRepository;
import ca.mcmaster.cas735.acmepark.visitor_identification.business.ParkingFeeTransactionRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParkingFeeTransactionRegistryTest {

    @Mock
    private ParkingFeeTransactionRepository database;

    @Mock
    private PaymentSender paymentManager;

    @Mock
    private ExitLot gateOpener;

    @InjectMocks
    private ParkingFeeTransactionRegistry parkingFeeTransactionRegistry;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIssueParkingFee() {
        // Arrange
        String visitorId = UUID.randomUUID().toString();
        String gateId = "Lot M";
        String licensePlate = "ABC123";
        LocalDateTime timestamp = LocalDateTime.now();

        ParkingFeeCreationData feeData = new ParkingFeeCreationData();
        feeData.setVisitorId(visitorId);
        feeData.setAmount(2000);
        feeData.setGateId(gateId);
        feeData.setLicensePlate(licensePlate);
        feeData.setTimestamp(timestamp);

        // Act
        parkingFeeTransactionRegistry.issueParkingFee(feeData);

        // Assert
        verify(database, times(1)).saveAndFlush(argThat(transaction ->
                transaction.getTransactionId() != null &&
                        transaction.getTransactionType() == TransactionType.PARKING_FEE &&
                        transaction.getTransactionStatus() == TransactionStatus.PENDING &&
                        transaction.getAmount().equals(2000) &&
                        transaction.getInitiatedBy().equals(visitorId) &&
                        transaction.getGateId().equals(gateId) &&
                        transaction.getLicensePlate().equals(licensePlate) &&
                        transaction.getTimestamp().equals(timestamp) &&
                        transaction.getUserType() == UserType.VISITOR
        ));
        verify(paymentManager, times(1)).sendTransaction(any(ParkingFeeTransaction.class));
    }

    @Test
    void testHandleParkingFeeStatusChanged_Success() {
        // Arrange
        String transactionId = UUID.randomUUID().toString();
        String gateId = "gate123";
        String licensePlate = "ABC123";

        ParkingFeeTransaction transaction = new ParkingFeeTransaction();
        transaction.setTransactionId(transactionId);
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transaction.setGateId(gateId);
        transaction.setLicensePlate(licensePlate);

        when(database.findByTransactionId(transactionId)).thenReturn(Optional.of(transaction));

        // Act
        parkingFeeTransactionRegistry.handleParkingFeeStatusChanged(transactionId);

        // Assert
        assertEquals(TransactionStatus.SUCCESS, transaction.getTransactionStatus());
        verify(database, times(1)).saveAndFlush(transaction);
        verify(gateOpener, times(1)).exitGateOpen(gateId, licensePlate);
    }

    @Test
    void testHandleParkingFeeStatusChanged_NotFound() {
        // Arrange
        String transactionId = UUID.randomUUID().toString();

        when(database.findByTransactionId(transactionId)).thenReturn(Optional.empty());

        // Act
        parkingFeeTransactionRegistry.handleParkingFeeStatusChanged(transactionId);

        // Assert
        verify(database, times(1)).findByTransactionId(transactionId);
        verify(database, never()).saveAndFlush(any());
        verify(gateOpener, never()).exitGateOpen(anyString(), anyString());
    }
}