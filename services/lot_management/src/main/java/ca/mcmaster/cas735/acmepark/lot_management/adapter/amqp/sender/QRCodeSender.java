package ca.mcmaster.cas735.acmepark.lot_management.adapter.amqp.sender;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.PrintQRcode;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.QRcodePrintSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class QRCodeSender implements QRcodePrintSender {
//    private final StreamBridge streamBridge;
//
//    @Autowired
//    public QRCodeSender(StreamBridge streamBridge) {
//        this.streamBridge = streamBridge;
//    }
//
//    @Override
//    public void sendQRcodePrint(PrintQRcode qrCode) {
//        streamBridge.send("printQRcodeSender-out-0", qrCode);
//    }
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.outbound-exchange-topic-qrcode}") private String exchange;

    public QRCodeSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @Override
    public void sendQRcodePrint(PrintQRcode qrCode) {
        log.debug("Sending message to {}: {}", exchange, qrCode);
        rabbitTemplate.convertAndSend(exchange, "*", translate(qrCode));
    }

    private String translate(PrintQRcode qrCode) {
        ObjectMapper mapper= new ObjectMapper();
        try {
            return mapper.writeValueAsString(qrCode);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Bean
    public TopicExchange outboundQRCode() {
        return new TopicExchange(exchange);
    }
}

