package ca.mcmaster.cas735.acmepark.lot_management.adapter.amqp.sender;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.AnalysisResult;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.LotUpdateSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class UpdateSender implements LotUpdateSender {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.outbound-exchange-topic-update}") private String exchange;

    public UpdateSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendUpdate(AnalysisResult analysisResult) {
        log.debug("Sending message to {}: {}", exchange, analysisResult);
        rabbitTemplate.convertAndSend(exchange, "*", translate(analysisResult));
    }

    private String translate(AnalysisResult analysisResult) {
        ObjectMapper mapper= new ObjectMapper();
        try {
            return mapper.writeValueAsString(analysisResult);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Bean
    public TopicExchange outboundUpdate() {
        return new TopicExchange(exchange);
    }
}
