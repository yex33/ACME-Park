package ca.mcmaster.cas735.acmepark.lot_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ca.mcmaster.cas735.acmepark.lot_management.business.ExitController;
import ca.mcmaster.cas735.acmepark.lot_management.business.internal.GenerateAnalysis;
import ca.mcmaster.cas735.acmepark.lot_management.business.internal.MaintainRecord;
import ca.mcmaster.cas735.acmepark.lot_management.dtos.AnalysisResult;
import ca.mcmaster.cas735.acmepark.lot_management.dtos.ControlGate;
import ca.mcmaster.cas735.acmepark.common.dtos.ExitGateRequest;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.ControlGateSender;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.LotUpdateSender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ExitControllerTests {
    @Mock
    private ControlGateSender controlGateSender;

    @Mock
    private MaintainRecord maintainer;

    @Mock
    private GenerateAnalysis analysis;

    @Mock
    private LotUpdateSender updateSender;

    @InjectMocks
    private ExitController exitController;

    private static ExitGateRequest exitGateRequest;
    private static AnalysisResult analysisResult;

    @BeforeAll
    static void setup() {
        exitGateRequest = new ExitGateRequest();
        exitGateRequest.setGateId("Lot M");
        exitGateRequest.setLicense("ABC-123");

        analysisResult = AnalysisResult.builder()
                .gateId("Lot M")
                .occupancy(1)
                .build();
    }

    @Test
    void shouldProcessExitSuccessfully() {
        when(analysis.generateAnalysis(exitGateRequest.getGateId()))
                .thenReturn(analysisResult);
        exitController.allowExit(exitGateRequest);

        verify(maintainer, times(1)).updateExitRecord(
                eq(exitGateRequest.getGateId()), eq(exitGateRequest.getLicense()));

        ArgumentCaptor<ControlGate> controlGateCaptor = ArgumentCaptor.forClass(ControlGate.class);

        verify(controlGateSender, times(1)).sendControlResult(controlGateCaptor.capture());

        ControlGate capturedControlGate = controlGateCaptor.getValue();
        assertEquals("Allow exit", capturedControlGate.getControlSignal());
        assertEquals(exitGateRequest.getGateId(), capturedControlGate.getGateId());

        verify(updateSender, times(1)).sendUpdate(eq(analysisResult));
    }


}
