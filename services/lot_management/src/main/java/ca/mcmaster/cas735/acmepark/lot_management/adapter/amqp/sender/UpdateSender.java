package ca.mcmaster.cas735.acmepark.lot_management.adapter.amqp.sender;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.AnalysisResult;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.LotUpdateSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class UpdateSender implements LotUpdateSender {
    private final StreamBridge streamBridge;

    public UpdateSender(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void sendUpdate(AnalysisResult analysisResult) {
        try {
            streamBridge.send("updateSender-out-0", analysisResult);
        } catch (Exception e) {
            log.error("Failed to send message to controlGateSender-out-0", e);
            throw new RuntimeException("Message sending failed", e);
        }

    }
}
