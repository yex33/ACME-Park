package ca.mcmaster.cas735.acmepark.lot_management.adapter.amqp.sender;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.PrintQRcode;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.QRcodePrintSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class QRCodeSender implements QRcodePrintSender {
    private final StreamBridge streamBridge;

    @Autowired
    public QRCodeSender(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void sendQRcodePrint(PrintQRcode qrCode) {
        try {
            streamBridge.send("qrCodeSender-out-0", qrCode);
        } catch (Exception e) {
            log.error("Failed to send message to controlGateSender-out-0", e);
            throw new RuntimeException("Message sending failed", e);
        }
    }
}

