package ca.mcmaster.cas735.acmepark.lot_management.adapter.amqp.sender;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.IssueUserFine;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.IssueUserFineSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class FineSender implements IssueUserFineSender {
    private final StreamBridge streamBridge;

    public FineSender(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void sendFine(IssueUserFine fineRequest) {
        try {
            streamBridge.send("fineSender-out-0", fineRequest);
        } catch (Exception e) {
            log.error("Failed to send message to controlGateSender-out-0", e);
            throw new RuntimeException("Message sending failed", e);
        }
    }
}
