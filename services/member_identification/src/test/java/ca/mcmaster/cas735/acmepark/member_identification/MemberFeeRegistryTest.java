package ca.mcmaster.cas735.acmepark.member_identification;

import ca.mcmaster.cas735.acmepark.common.dtos.TransactionStatus;
import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.MemberFeeTransaction;
import ca.mcmaster.cas735.acmepark.member_identification.dto.MemberFeeCreationData;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.MonitorDataSender;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.TransponderManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.required.MemberFeeTransactionRepository;
import ca.mcmaster.cas735.acmepark.member_identification.business.MemberFeeRegistry;
import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
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

class MemberFeeRegistryTest {

    @Mock
    private MemberFeeTransactionRepository database;

    @Mock
    private TransponderManagement transponderManager;

    @Mock
    private MonitorDataSender monitorDataSender;

    @InjectMocks
    private MemberFeeRegistry memberFeeRegistry;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransaction() {
        // Arrange
        String organizationId = UUID.randomUUID().toString();
        String permitId = UUID.randomUUID().toString();

        MemberFeeCreationData request = new MemberFeeCreationData();
        request.setAmount(30000);
        request.setUserType(UserType.STUDENT);
        request.setOrganizationId(organizationId);
        request.setTimestamp(LocalDateTime.now());
        request.setDescription("Permit fee");
        request.setAssociatedPermitId(permitId);

        MemberFeeTransaction savedTransaction = new MemberFeeTransaction();
        savedTransaction.setTransactionId(UUID.randomUUID().toString());
        savedTransaction.setTransactionType(TransactionType.MEMBER_FEE);
        savedTransaction.setTransactionStatus(TransactionStatus.PENDING);
        savedTransaction.setAmount(request.getAmount());
        savedTransaction.setUserType(request.getUserType());
        savedTransaction.setInitiatedBy(request.getOrganizationId());
        savedTransaction.setTimestamp(request.getTimestamp());
        savedTransaction.setDescription(request.getDescription());
        savedTransaction.setAssociatedPermitId(request.getAssociatedPermitId());

        when(database.saveAndFlush(any(MemberFeeTransaction.class))).thenReturn(savedTransaction);

        // Act
        MemberFeeTransaction result = memberFeeRegistry.createTransaction(request);

        // Assert
        assertNotNull(result.getTransactionId());
        assertEquals(TransactionType.MEMBER_FEE, result.getTransactionType());
        assertEquals(TransactionStatus.PENDING, result.getTransactionStatus());
        assertEquals(30000, result.getAmount());
        verify(database, times(1)).saveAndFlush(any(MemberFeeTransaction.class));
    }

    @Test
    void testCompleteTransaction() {
        // Arrange
        String transactionId = UUID.randomUUID().toString();
        String permitId = UUID.randomUUID().toString();

        MemberFeeTransaction transaction = new MemberFeeTransaction();
        transaction.setTransactionId(transactionId);
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transaction.setAssociatedPermitId(permitId);

        when(database.findByTransactionId(transactionId)).thenReturn(Optional.of(transaction));

        // Act
        memberFeeRegistry.completeTransaction(transactionId);

        // Assert
        assertEquals(TransactionStatus.SUCCESS, transaction.getTransactionStatus());
        verify(database, times(1)).saveAndFlush(transaction);
        verify(transponderManager, times(1)).issueTransponderByPermitId(permitId);
        verify(monitorDataSender, times(1)).sendPermitSale(permitId);
    }

    @Test
    void testCompleteTransaction_NotFound() {
        // Arrange
        String transactionId = UUID.randomUUID().toString();

        when(database.findByTransactionId(transactionId)).thenReturn(Optional.empty());

        // Act
        memberFeeRegistry.completeTransaction(transactionId);

        // Assert
        verify(database, never()).saveAndFlush(any());
        verify(transponderManager, never()).issueTransponderByPermitId(anyString());
        verify(monitorDataSender, never()).sendPermitSale(anyString());
    }
}