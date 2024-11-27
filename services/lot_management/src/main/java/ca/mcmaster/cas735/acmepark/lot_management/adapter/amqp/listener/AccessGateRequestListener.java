package ca.mcmaster.cas735.acmepark.lot_management.adapter.amqp.listener;

import ca.mcmaster.cas735.acmepark.common.dtos.AccessGateRequest;
import ca.mcmaster.cas735.acmepark.lot_management.port.provided.AccessGateRequestReceiver;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class AccessGateRequestListener {
    private final AccessGateRequestReceiver decider;

    @Autowired
    public AccessGateRequestListener(AccessGateRequestReceiver decider) {
        this.decider = decider;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "access.gate.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.inbound-exchange-topic-access}",
                    ignoreDeclarationExceptions = "true", type = "topic"),
            key = "*"))
    public void listen(String data){
        log.debug("Receiving access request {}", data);
        AccessGateRequest request = translate(data);
        decider.checkRule(request);
    }

    private AccessGateRequest translate (String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            return mapper.readValue(raw, AccessGateRequest.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
