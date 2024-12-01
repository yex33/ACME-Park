package ca.mcmaster.cas735.acmepark.lot_management.business;

import ca.mcmaster.cas735.acmepark.lot_management.business.internal.GenerateAnalysis;
import ca.mcmaster.cas735.acmepark.lot_management.business.internal.MaintainRecord;
import ca.mcmaster.cas735.acmepark.lot_management.dtos.ControlGate;
import ca.mcmaster.cas735.acmepark.common.dtos.ExitGateRequest;
import ca.mcmaster.cas735.acmepark.lot_management.port.provided.ExitGateRequestReceiver;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.ControlGateSender;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.LotUpdateSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service @Slf4j
@AllArgsConstructor
public class ExitController implements ExitGateRequestReceiver {
    private final ControlGateSender controlGateSender;
    private final MaintainRecord maintainer;
    private final GenerateAnalysis analysis;
    private final LotUpdateSender updateSender;

    @Override
    public void allowExit(ExitGateRequest exitRequest) {
        log.info("Processing exit request for license: {}, gate: {}", exitRequest.getLicense(), exitRequest.getGateId());
        maintainer.updateExitRecord(exitRequest.getGateId(), exitRequest.getLicense());

        ControlGate exitSignal = new ControlGate();
        exitSignal.setControlSignal("Allow exit");
        exitSignal.setGateId(exitRequest.getGateId());
        controlGateSender.sendControlResult(exitSignal);
        log.info("Gate opened for vehicle: {}, gate: {}", exitRequest.getLicense(), exitRequest.getGateId());

        updateSender.sendUpdate(analysis.generateAnalysis(exitRequest.getGateId()));
        log.info("Real-time occupancy update sent for gate: {}", exitRequest.getGateId());
    }
}
