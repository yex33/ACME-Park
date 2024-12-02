package ca.mcmaster.cas735.acmepark.visitor_identification;

import ca.mcmaster.cas735.acmepark.visitor_identification.business.entities.Voucher;
import ca.mcmaster.cas735.acmepark.visitor_identification.business.errors.AlreadyExistingException;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.required.VoucherDataRepository;
import ca.mcmaster.cas735.acmepark.visitor_identification.business.VoucherRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VoucherRegistryTest {

    @Mock
    private VoucherDataRepository database;

    @InjectMocks
    private VoucherRegistry voucherRegistry;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIssueVoucher_Success() throws AlreadyExistingException {
        // Arrange
        String licensePlate = "ABC123";

        when(database.findVoucherByLicensePlateAndConsumedFalse(licensePlate)).thenReturn(null);

        // Act
        String voucherId = voucherRegistry.issueVoucher(licensePlate);

        // Assert
        assertNotNull(voucherId);
        verify(database, times(1)).saveAndFlush(argThat(voucher ->
                voucher.getVoucherId() != null &&
                        voucher.getLicensePlate().equals(licensePlate) &&
                        !voucher.getConsumed()
        ));
    }

    @Test
    void testIssueVoucher_AlreadyExists() {
        // Arrange
        String licensePlate = "ABC123";

        Voucher existingVoucher = new Voucher();
        existingVoucher.setVoucherId(UUID.randomUUID().toString());
        existingVoucher.setLicensePlate(licensePlate);
        existingVoucher.setConsumed(false);

        when(database.findVoucherByLicensePlateAndConsumedFalse(licensePlate)).thenReturn(existingVoucher);

        // Act & Assert
        AlreadyExistingException exception = assertThrows(AlreadyExistingException.class, () -> {
            voucherRegistry.issueVoucher(licensePlate);
        });

        assertEquals("Voucher identified by id (ABC123) for key (licensePlate) already exists", exception.getMessage());
        verify(database, never()).saveAndFlush(any(Voucher.class));
    }

    @Test
    void testRedeemVoucher_Success() {
        // Arrange
        String voucherId = UUID.randomUUID().toString();
        String licensePlate = "ABC123";

        Voucher voucher = new Voucher();
        voucher.setVoucherId(voucherId);
        voucher.setLicensePlate(licensePlate);
        voucher.setConsumed(false);

        when(database.findVoucherByLicensePlateAndConsumedFalse(licensePlate)).thenReturn(voucher);

        // Act
        Boolean result = voucherRegistry.redeemVoucher(voucherId, licensePlate);

        // Assert
        assertTrue(result);
        assertTrue(voucher.getConsumed());
        verify(database, times(1)).saveAndFlush(voucher);
    }

    @Test
    void testRedeemVoucher_NoVoucherFound() {
        // Arrange
        String voucherId = UUID.randomUUID().toString();
        String licensePlate = "ABC123";

        when(database.findVoucherByLicensePlateAndConsumedFalse(licensePlate)).thenReturn(null);

        // Act
        Boolean result = voucherRegistry.redeemVoucher(voucherId, licensePlate);

        // Assert
        assertFalse(result);
        verify(database, never()).saveAndFlush(any(Voucher.class));
    }

    @Test
    void testRedeemVoucher_LicensePlateMismatch() {
        // Arrange
        String voucherId = UUID.randomUUID().toString();
        String licensePlate = "ABC123";

        Voucher voucher = new Voucher();
        voucher.setVoucherId(voucherId);
        voucher.setLicensePlate("XYZ789");
        voucher.setConsumed(false);

        when(database.findVoucherByLicensePlateAndConsumedFalse(licensePlate)).thenReturn(voucher);

        // Act
        Boolean result = voucherRegistry.redeemVoucher(voucherId, licensePlate);

        // Assert
        assertFalse(result);
        verify(database, never()).saveAndFlush(any(Voucher.class));
    }
}