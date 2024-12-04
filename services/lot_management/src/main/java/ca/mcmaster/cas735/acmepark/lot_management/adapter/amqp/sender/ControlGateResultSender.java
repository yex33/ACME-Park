package ca.mcmaster.cas735.acmepark.lot_management.adapter.amqp.sender;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.ControlGate;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.ControlGateSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class ControlGateResultSender implements ControlGateSender {
    private final StreamBridge streamBridge;

    @Autowired
    public ControlGateResultSender(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void sendControlResult(ControlGate controlResult) {
        try {
            streamBridge.send("controlGateSender-out-0", controlResult);
            log.info("Gate control result: {}", controlResult);
        } catch (Exception e) {
            log.error("Failed to send message to controlGateSender-out-0", e);
            throw new RuntimeException("Message sending failed", e);
        }
    }

}
