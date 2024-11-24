package ca.mcmaster.cas735.acmepark.lot_management.adapter.amqp;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.IssueUserFine;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.IssueUserFineSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class FineSender implements IssueUserFineSender {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.outbound-exchange-topic-fine}") private String exchange;

    @Autowired
    public FineSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendFine(IssueUserFine fineRequest) {
        log.debug("Sending message to {}: {}", exchange, fineRequest);
        rabbitTemplate.convertAndSend(exchange, "*", translate(fineRequest));
    }

    private String translate(IssueUserFine fineRequest) {
        ObjectMapper mapper= new ObjectMapper();
        try {
            return mapper.writeValueAsString(fineRequest);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Bean
    public TopicExchange outboundFine() {
        return new TopicExchange(exchange);
    }
}
