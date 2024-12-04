package ca.mcmaster.cas735.acmepark.member_identification;

import ca.mcmaster.cas735.acmepark.common.dtos.AccessGateRequest;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.Permit;
import ca.mcmaster.cas735.acmepark.member_identification.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.member_identification.dto.TransponderAccessData;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.GateManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.required.PermitDataRepository;
import ca.mcmaster.cas735.acmepark.member_identification.business.TransponderManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransponderManagerTest {

    @Mock
    private PermitDataRepository database;

    @Mock
    private GateManagement gateManager;

    @InjectMocks
    private TransponderManager transponderManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRequestGateOpen_ValidPermit() throws NotFoundException {
        // Arrange
        String transponderId = UUID.randomUUID().toString();
        String gateId = "Lot M";
        String licensePlate = "ABC123";
        String organizationId = UUID.randomUUID().toString();

        Permit permit = new Permit();
        permit.setPermitId(UUID.randomUUID().toString());
        permit.setTransponderId(transponderId);
        permit.setOrganizationId(organizationId);
        permit.setStartDate(LocalDate.now().minusDays(1));
        permit.setExpiryDate(LocalDate.now().plusDays(30));

        when(database.findPermitByTransponderId(transponderId)).thenReturn(permit);

        TransponderAccessData data = new TransponderAccessData();
        data.setTransponderId(transponderId);
        data.setGateId(gateId);
        data.setLicensePlate(licensePlate);

        // Act
        transponderManager.requestGateOpen(data);

        // Assert
        verify(database, times(1)).findPermitByTransponderId(transponderId);
        verify(gateManager, times(1)).requestGateOpen(any(AccessGateRequest.class));
    }

    @Test
    void testRequestGateOpen_ExpiredPermit() throws NotFoundException {
        // Arrange
        String transponderId = UUID.randomUUID().toString();

        Permit permit = new Permit();
        permit.setPermitId(UUID.randomUUID().toString());
        permit.setTransponderId(transponderId);
        permit.setStartDate(LocalDate.now().minusDays(30));
        permit.setExpiryDate(LocalDate.now().minusDays(1)); // Expired permit

        when(database.findPermitByTransponderId(transponderId)).thenReturn(permit);

        TransponderAccessData data = new TransponderAccessData();
        data.setTransponderId(transponderId);

        // Act
        transponderManager.requestGateOpen(data);

        // Assert
        verify(database, times(1)).findPermitByTransponderId(transponderId);
        verify(gateManager, never()).requestGateOpen(any());
    }

    @Test
    void testRequestGateOpen_NoPermitFound() throws NotFoundException {
        // Arrange
        String transponderId = UUID.randomUUID().toString();

        when(database.findPermitByTransponderId(transponderId)).thenReturn(null);

        TransponderAccessData data = new TransponderAccessData();
        data.setTransponderId(transponderId);

        // Act
        transponderManager.requestGateOpen(data);

        // Assert
        verify(database, times(1)).findPermitByTransponderId(transponderId);
        verify(gateManager, never()).requestGateOpen(any());
    }

    @Test
    void testIssueTransponderByPermitId() {
        // Arrange
        String permitId = UUID.randomUUID().toString();

        Permit permit = new Permit();
        permit.setPermitId(permitId);

        when(database.findPermitByPermitId(permitId)).thenReturn(permit);

        // Act
        transponderManager.issueTransponderByPermitId(permitId);

        // Assert
        verify(database, times(1)).findPermitByPermitId(permitId);
        verify(database, times(1)).saveAndFlush(permit);
        assertNotNull(permit.getTransponderId());
        assertTrue(permit.getProcessed());
        assertEquals(LocalDate.now(), permit.getStartDate());
        assertEquals(LocalDate.now().plusYears(1), permit.getExpiryDate());
    }

    @Test
    void testIssueTransponderByPermitId_NoPermitFound() {
        // Arrange
        String permitId = UUID.randomUUID().toString();

        when(database.findPermitByPermitId(permitId)).thenReturn(null);

        // Act
        transponderManager.issueTransponderByPermitId(permitId);

        // Assert
        verify(database, times(1)).findPermitByPermitId(permitId);
        verify(database, never()).saveAndFlush(any());
    }
}