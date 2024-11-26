package ca.mcmaster.cas735.acmepark.lot_management.port.required;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.AnalysisResult;

public interface LotUpdateSender {
    void sendUpdate(AnalysisResult analysisResult);
}
