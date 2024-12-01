package ca.mcmaster.cas735.acmepark.lot_management;


import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import ca.mcmaster.cas735.acmepark.lot_management.business.DataAnalysis;
import ca.mcmaster.cas735.acmepark.lot_management.business.entities.EntryRecord;
import ca.mcmaster.cas735.acmepark.lot_management.business.entities.ExitRecord;
import ca.mcmaster.cas735.acmepark.lot_management.dtos.AnalysisResult;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.ExitRecordDataRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataAnalysisTests {
    @Mock
    private ExitRecordDataRepository exitDb;

    @InjectMocks
    private DataAnalysis dataAnalysis;

    private static String gateId;
    private static UserType userType;
    private static ExitRecord exitRecord;
    private static EntryRecord entryRecord;

    @BeforeAll
    static void setup() {
        gateId = "Lot M";
        userType = UserType.STUDENT;
        entryRecord = EntryRecord.builder()
                .entryRecordId(UUID.randomUUID().toString())
                .entryTime(LocalDateTime.now())
                .gateId(gateId)
                .userId(UUID.randomUUID().toString())
                .userType(userType)
                .licensePlate("ABC-123")
                .build();

        exitRecord = ExitRecord.builder()
                .exitRecordId(UUID.randomUUID().toString())
                .entryRecord(entryRecord)
                .exitTime(null)
                .gateId(gateId)
                .userType(userType)
                .licensePlate("ABC-123")
                .build();
    }

    @Test
    void shouldReturnCorrectAnalysisResultForValidGateId() {
        when(exitDb.findByExitTimeIsNullAndGateId(gateId)).thenReturn(List.of(exitRecord));

        AnalysisResult result = dataAnalysis.generateAnalysis(gateId);
        assertEquals(1, result.getOccupancy());
        assertEquals(gateId, result.getGateId());
        verify(exitDb, times(1)).findByExitTimeIsNullAndGateId(gateId);
    }

    @Test
    void shouldReturnEmptyAnalysisResultForNoRecords() {
        when(exitDb.findByExitTimeIsNullAndGateId(gateId)).thenReturn(List.of());

        AnalysisResult result = dataAnalysis.generateAnalysis(gateId);

        assertEquals(0, result.getOccupancy());
        assertEquals(gateId, result.getGateId());
        verify(exitDb, times(1)).findByExitTimeIsNullAndGateId(gateId);
    }
}
