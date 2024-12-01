package ca.mcmaster.cas735.acmepark.lot_management;

import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import ca.mcmaster.cas735.acmepark.lot_management.business.RecordManager;
import ca.mcmaster.cas735.acmepark.lot_management.business.entities.EntryRecord;
import ca.mcmaster.cas735.acmepark.lot_management.business.entities.ExitRecord;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.EntryRecordDataRepository;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.ExitRecordDataRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecordManagerTests {
    @Mock
    private EntryRecordDataRepository entryDb;

    @Mock
    private ExitRecordDataRepository exitDb;

    @InjectMocks
    private RecordManager recordManager;

    private static String licensePlate;
    private static String gateId;
    private static UserType userType;

    private static EntryRecord entryRecord;
    private static ExitRecord exitRecord;
    private static String userId;

    @BeforeAll
    static void setup() {
        licensePlate = "ABC-123";
        gateId = "Lot M";
        userType = UserType.STUDENT;
        userId = UUID.randomUUID().toString();
        entryRecord = EntryRecord.builder()
                .entryRecordId(UUID.randomUUID().toString())
                .userId(userId)
                .licensePlate(licensePlate)
                .userType(userType)
                .gateId(gateId)
                .entryTime(LocalDateTime.now())
                .build();

        exitRecord = ExitRecord.builder()
                .exitRecordId(UUID.randomUUID().toString())
                .entryRecord(entryRecord)
                .userId(userId)
                .licensePlate(licensePlate)
                .gateId(gateId)
                .exitTime(null)
                .build();
    }

    @Test
    void shouldInsertRecordsSuccessfully() {
        recordManager.insertRecord(licensePlate, userId, userType, gateId);

        verify(entryDb, times(1)).save(argThat(record ->
                record.getLicensePlate().equals(licensePlate) &&
                        record.getUserId().equals(userId) &&
                        record.getUserType().equals(userType) &&
                        record.getGateId().equals(gateId)
        ));

        verify(exitDb, times(1)).save(argThat(record ->
                record.getLicensePlate().equals(licensePlate) &&
                        record.getUserId().equals(userId) &&
                        record.getGateId().equals(gateId)
        ));
    }

    @Test
    void shouldUpdateExitRecordSuccessfully() {
        entryRecord.setExitRecord(exitRecord);
        when(entryDb.findByLicensePlateAndGateIdAndExitRecord_ExitTimeIsNull(licensePlate, gateId))
                .thenReturn(Optional.of(entryRecord));
        recordManager.updateExitRecord(gateId, licensePlate);

        verify(exitDb, times(1)).save(argThat(record ->
                record.getLicensePlate().equals(licensePlate) &&
                        record.getUserId().equals(userId) &&
                        record.getGateId().equals(gateId) &&
                        record.getExitTime() != null
        ));
    }

    @Test
    void shouldThrowExceptionWhenEntryRecordNotFoundDuringUpdate() {
        when(entryDb.findByLicensePlateAndGateIdAndExitRecord_ExitTimeIsNull(licensePlate, gateId))
                .thenReturn(Optional.empty());

        recordManager.updateExitRecord(gateId, licensePlate);
        verify(exitDb, never()).save(any());
        verify(entryDb, times(1))
                .findByLicensePlateAndGateIdAndExitRecord_ExitTimeIsNull(licensePlate, gateId);
    }
}
