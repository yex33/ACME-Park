package ca.mcmaster.cas735.acmepark.visitor_identification;

import ca.mcmaster.cas735.acmepark.visitor_identification.dto.AccessGateRequest;
import ca.mcmaster.cas735.acmepark.visitor_identification.business.entities.Visitor;
import ca.mcmaster.cas735.acmepark.visitor_identification.dto.ParkingFeeCreationData;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.GateOpener;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.ParkingFeeManagement;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.VoucherManagement;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.required.VisitorDataRepository;
import ca.mcmaster.cas735.acmepark.visitor_identification.business.VisitorRegistry;
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

class VisitorRegistryTest {

    @Mock
    private VisitorDataRepository database;

    @Mock
    private GateOpener gateManager;

    @Mock
    private VoucherManagement voucherManager;

    @Mock
    private ParkingFeeManagement parkingFeeManager;

    @InjectMocks
    private VisitorRegistry visitorRegistry;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRequestAccess() {
        // Arrange
        String licensePlate = "ABC123";
        String gateId = "Lot M";

        Visitor invalidVisitor = new Visitor();
        invalidVisitor.setVisitorId(UUID.randomUUID().toString());
        invalidVisitor.setLicensePlate(licensePlate);
        invalidVisitor.setExited(false);

        List<Visitor> invalidVisitors = new ArrayList<>();
        invalidVisitors.add(invalidVisitor);

        when(database.findVisitorsByLicensePlateAndExitedFalse(licensePlate)).thenReturn(invalidVisitors);

        // Act
        visitorRegistry.requestAccess(licensePlate, gateId);

        // Assert
        verify(database, times(1)).delete(invalidVisitor);
        verify(database, times(1)).saveAndFlush(any(Visitor.class));
        verify(gateManager, times(1)).requestGateOpen(any(AccessGateRequest.class));
    }

    @Test
    void testExit_WithVoucher() {
        // Arrange
        String visitorId = UUID.randomUUID().toString();
        String licensePlate = "ABC123";
        String voucherId = UUID.randomUUID().toString();

        Visitor visitor = new Visitor();
        visitor.setVisitorId(visitorId);
        visitor.setLicensePlate(licensePlate);
        visitor.setAccessTime(LocalDateTime.now().minusHours(2));
        visitor.setExited(false);

        when(database.findVisitorByVisitorId(visitorId)).thenReturn(visitor);

        // Act
        visitorRegistry.exit(visitorId, licensePlate, voucherId);

        // Assert
        verify(voucherManager, times(1)).redeemVoucher(voucherId, licensePlate);
        verify(database, times(1)).saveAndFlush(visitor);
        verify(parkingFeeManager, times(1)).issueParkingFee(any(ParkingFeeCreationData.class));
        assertTrue(visitor.getExited());
    }

    @Test
    void testExit_WithoutVoucher() {
        // Arrange
        String visitorId = UUID.randomUUID().toString();
        String licensePlate = "ABC123";

        Visitor visitor = new Visitor();
        visitor.setVisitorId(visitorId);
        visitor.setLicensePlate(licensePlate);
        visitor.setAccessTime(LocalDateTime.now().minusHours(2));
        visitor.setExited(false);

        when(database.findVisitorByVisitorId(visitorId)).thenReturn(visitor);

        // Act
        visitorRegistry.exit(visitorId, licensePlate, null);

        // Assert
        verify(voucherManager, never()).redeemVoucher(anyString(), anyString());
        verify(database, times(1)).saveAndFlush(visitor);
        verify(parkingFeeManager, times(1)).issueParkingFee(any(ParkingFeeCreationData.class));
        assertTrue(visitor.getExited());
    }

    @Test
    void testExit_VisitorNotFound() {
        // Arrange
        String visitorId = UUID.randomUUID().toString();

        when(database.findVisitorByVisitorId(visitorId)).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            visitorRegistry.exit(visitorId, "ABC123", null);
        });

        assertEquals("Visitor not found.", exception.getMessage());
        verify(database, never()).saveAndFlush(any());
        verify(parkingFeeManager, never()).issueParkingFee(any());
        verify(voucherManager, never()).redeemVoucher(anyString(), anyString());
    }
}