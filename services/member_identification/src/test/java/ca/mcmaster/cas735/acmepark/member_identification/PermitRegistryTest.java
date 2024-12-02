package ca.mcmaster.cas735.acmepark.member_identification;

import ca.mcmaster.cas735.acmepark.common.dtos.ParkingPermitInfo;
import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import ca.mcmaster.cas735.acmepark.member_identification.business.PermitRegistry;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.MemberFeeTransaction;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.Permit;
import ca.mcmaster.cas735.acmepark.member_identification.business.errors.AlreadyExistingException;
import ca.mcmaster.cas735.acmepark.member_identification.dto.MemberFeeCreationData;
import ca.mcmaster.cas735.acmepark.member_identification.dto.PermitCreationData;
import ca.mcmaster.cas735.acmepark.member_identification.ports.required.PermitDataRepository;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.MemberFeeManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.PaymentSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermitRegistryTest {

    @Mock
    private PermitDataRepository database;

    @Mock
    private MemberFeeManagement feeManager;

    @Mock
    private PaymentSender paymentManager;

    @InjectMocks
    private PermitRegistry permitRegistry;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_Success() throws AlreadyExistingException {
        String organizationId = UUID.randomUUID().toString();
        PermitCreationData request = new PermitCreationData();
        request.setOrganizationId(organizationId);
        request.setUserType(UserType.STUDENT);
        request.setIsRenew(false);

        Permit permit = request.asPermit();
        permit.setPermitId(UUID.randomUUID().toString());

        MemberFeeTransaction transaction = new MemberFeeTransaction();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setAmount(30000);
        transaction.setUserType(UserType.STUDENT);

        when(database.existsByOrganizationId(organizationId)).thenReturn(false); // No existing permit
        when(database.saveAndFlush(any(Permit.class))).thenReturn(permit); // Save permit
        when(feeManager.createTransaction(any(MemberFeeCreationData.class))).thenReturn(transaction); // Mock transaction creation

        permitRegistry.create(request);

        verify(database).saveAndFlush(any(Permit.class));
        verify(paymentManager).sendTransaction(transaction);
    }

    @Test
    void testFindAll() {
        List<Permit> permits = new ArrayList<>();
        Permit permit1 = new Permit();
        permit1.setPermitId(UUID.randomUUID().toString()); // Generate valid UUID for permitId
        permit1.setOrganizationId(UUID.randomUUID().toString()); // Generate valid UUID for organizationId
        permit1.setUserType(UserType.STUDENT);
        permits.add(permit1);

        when(database.findAll()).thenReturn(permits);

        List<ParkingPermitInfo> permitInfos = permitRegistry.findAll();

        assertEquals(1, permitInfos.size());
        assertEquals(permit1.getPermitId(), permitInfos.get(0).getPermitId());
        assertEquals(permit1.getOrganizationId(), permitInfos.get(0).getOrganizationId());
        assertEquals(permit1.getUserType(), permitInfos.get(0).getUserType());
    }
}