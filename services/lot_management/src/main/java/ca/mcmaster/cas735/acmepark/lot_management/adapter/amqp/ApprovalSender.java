package ca.mcmaster.cas735.acmepark.lot_management.adapter.amqp;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.AccessApproval;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.AccessApprovalSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class ApprovalSender implements AccessApprovalSender {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.outbound-exchange-topic-approval}") private String exchange;

    public ApprovalSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendApproval(AccessApproval accessApproval) {
        log.debug("Sending message to {}: {}", exchange, accessApproval);
        rabbitTemplate.convertAndSend(exchange, "*", translate(accessApproval));
    }

    private String translate(AccessApproval accessApproval) {
        ObjectMapper mapper= new ObjectMapper();
        try {
            return mapper.writeValueAsString(accessApproval);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Bean
    public TopicExchange outboundApproval() {
        return new TopicExchange(exchange);
    }
}
