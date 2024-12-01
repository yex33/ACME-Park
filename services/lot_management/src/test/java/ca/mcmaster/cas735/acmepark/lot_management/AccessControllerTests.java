package ca.mcmaster.cas735.acmepark.lot_management;

import ca.mcmaster.cas735.acmepark.common.dtos.AccessGateRequest;
import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import ca.mcmaster.cas735.acmepark.lot_management.adapter.amqp.sender.QRCodeSender;
import ca.mcmaster.cas735.acmepark.lot_management.business.AccessController;
import ca.mcmaster.cas735.acmepark.lot_management.business.entities.AccessRule;
import ca.mcmaster.cas735.acmepark.lot_management.business.internal.GenerateAnalysis;
import ca.mcmaster.cas735.acmepark.lot_management.business.internal.MaintainRecord;
import ca.mcmaster.cas735.acmepark.lot_management.dtos.PrintQRcode;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.ControlGateSender;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.LotUpdateSender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccessControllerTests {
    @Mock
    private AccessRule accessRule;

    @Mock
    private QRCodeSender qrSender;

    @Mock
    private MaintainRecord maintainer;

    @Mock
    private ControlGateSender controlGateSender;

    @Mock
    private GenerateAnalysis analysis;

    @Mock
    private LotUpdateSender updateSender;

    @InjectMocks
    private AccessController accessController;

    static String gateId;
    static String licensePlate;
    static String userId;

    @BeforeAll
    static void setup() {
        gateId = "Lot C";
        licensePlate = "license1";
        userId = UUID.randomUUID().toString();
    }

    @Test
    void shouldGrantAccessForValidUser() {
        AccessGateRequest request = new AccessGateRequest();
        request.setGateId(gateId);
        request.setLicense(licensePlate);
        request.setUserType(UserType.VISITOR);
        request.setUserId(userId);

        accessController.checkRule(request);

        ArgumentCaptor<PrintQRcode> captor = ArgumentCaptor.forClass(PrintQRcode.class);
        verify(qrSender, times(1)).sendQRcodePrint(captor.capture());
        PrintQRcode captorQR = captor.getValue();
        assertEquals(licensePlate, captorQR.getLicense());

        verify(controlGateSender, times(1)).sendControlResult(argThat(signal ->
                signal.getControlSignal().equals("Access Approval") && signal.getGateId().equals("Lot C")));

        verify(maintainer, times(1)).insertRecord(eq("license1"), anyString(), eq(UserType.VISITOR), eq("Lot C"));
    }

    @Test
    void shouldDenyAccessWhenUserTypeNotAllowed() {
        // Set to the invalid user type for Lot C
        AccessGateRequest request = new AccessGateRequest(); // Create a fresh instance
        request.setGateId(gateId);
        request.setLicense(licensePlate);
        request.setUserType(UserType.STUDENT); // Not allowed user type
        request.setUserId(userId);

        accessController.checkRule(request);

        verify(controlGateSender, times(1)).sendControlResult(argThat(signal ->
                signal.getControlSignal().equals("Access Deny") &&
                        signal.getGateId().equals(gateId)
        ));

        verify(qrSender, never()).sendQRcodePrint(any());

        verify(maintainer, never()).insertRecord(any(), any(), any(), any());
    }

    @Test
    void shouldThrowExceptionWhenAccessRequestIsNull() {
        assertThrows(NullPointerException.class, () -> accessController.checkRule(null));
        verifyNoInteractions(qrSender, maintainer, controlGateSender, updateSender);
    }
}
