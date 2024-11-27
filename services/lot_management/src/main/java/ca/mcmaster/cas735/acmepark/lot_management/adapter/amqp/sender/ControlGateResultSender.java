package ca.mcmaster.cas735.acmepark.lot_management.adapter.amqp.sender;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.ControlGate;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.ControlGateSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class ControlGateResultSender implements ControlGateSender {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.outbound-exchange-topic-control}") private String exchange;

    public ControlGateResultSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendControlResult(ControlGate accessResult) {
        log.debug("Sending message to {}: {}", exchange, accessResult);
        rabbitTemplate.convertAndSend(exchange, "*", translate(accessResult));
    }

    private String translate(ControlGate accessResult) {
        ObjectMapper mapper= new ObjectMapper();
        try {
            return mapper.writeValueAsString(accessResult);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Bean
    public TopicExchange outboundControl() {
        return new TopicExchange(exchange);
    }
}
