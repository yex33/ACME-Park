package ca.mcmaster.cas735.acmepark.lot_management;

import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import ca.mcmaster.cas735.acmepark.lot_management.business.UserNavigator;
import ca.mcmaster.cas735.acmepark.lot_management.business.entities.EntryRecord;
import ca.mcmaster.cas735.acmepark.lot_management.dtos.IssueUserFine;
import ca.mcmaster.cas735.acmepark.lot_management.dtos.IssueVehicleFine;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.EntryRecordDataRepository;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.IssueUserFineSender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserNavigatorTests {
    @Mock
    EntryRecordDataRepository entryRecordDataRepository;

    @Mock
    IssueUserFineSender issueUserFineSender;

    @InjectMocks
    UserNavigator userNavigator;

    static String licensePlate;
    static EntryRecord entryRecord;
    static IssueVehicleFine issueVehicleFine;

    @BeforeAll
    static void setup() {
        licensePlate = "squeeze";
        entryRecord = EntryRecord.builder()
                .entryRecordId(UUID.randomUUID().toString())
                .entryTime(LocalDateTime.now())
                .gateId("lot M")
                .userType(UserType.STUDENT)
                .userId(UUID.randomUUID().toString())
                .licensePlate(licensePlate).build();
        issueVehicleFine = new IssueVehicleFine();
        issueVehicleFine.setLicensePlate(licensePlate);
        issueVehicleFine.setFine("400");
    }

    @Test
    void shouldSendFineWithCorrectAmount() {
        when(entryRecordDataRepository.findByLicensePlate(anyString()))
                .thenReturn(Optional.of(entryRecord));

        userNavigator.issueFine(issueVehicleFine);

        ArgumentCaptor<IssueUserFine> captor = ArgumentCaptor.forClass(IssueUserFine.class);
        verify(issueUserFineSender, times(1)).sendFine(captor.capture());

        IssueUserFine capturedFine = captor.getValue();
        assertEquals("400", capturedFine.getFine());
        assertEquals(entryRecord.getUserId(), capturedFine.getUserID());
    }

    @Test
    void shouldNotIssueFineWhenNoRecordIsFound() {
        when(entryRecordDataRepository.findByLicensePlate(anyString())).thenReturn(Optional.empty());
        userNavigator.issueFine(issueVehicleFine);
        verify(issueUserFineSender, never()).sendFine(any());
    }

    @Test
    void shouldHandleUnexpectedException() {
        when(entryRecordDataRepository.findByLicensePlate(anyString()))
                .thenThrow(new RuntimeException("Unexpected database error"));
        assertDoesNotThrow(() -> userNavigator.issueFine(issueVehicleFine));
        verify(issueUserFineSender, never()).sendFine(any());
    }
}
