package ca.mcmaster.cas735.acmepark.lot_management.business;

import ca.mcmaster.cas735.acmepark.lot_management.business.entities.EntryRecord;
import ca.mcmaster.cas735.acmepark.lot_management.business.entities.ExitRecord;
import ca.mcmaster.cas735.acmepark.lot_management.business.internal.GenerateAnalysis;
import ca.mcmaster.cas735.acmepark.lot_management.dtos.AnalysisResult;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.ExitRecordDataRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @Slf4j
@AllArgsConstructor
public class DataAnalysis implements GenerateAnalysis {
    private final ExitRecordDataRepository exitDb;

    @Override
    public AnalysisResult generateAnalysis(String gateId) {
        List<ExitRecord> exitRecordsWithNullTime = exitDb.findByExitTimeIsNullAndGateId(gateId);
        List<EntryRecord> occupiedRecords =
                exitRecordsWithNullTime
                        .stream()
                        .map(ExitRecord::getEntryRecord)
                        .toList();
        log.debug("Analysis generated");
        return AnalysisResult.builder().occupancy(occupiedRecords.size()).gateId(gateId).build();
    }
}
