package ca.mcmaster.cas735.acmepark.lot_management.business.internal;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.AnalysisResult;

public interface GenerateAnalysis {
    AnalysisResult generateAnalysis(String gateId);
}
