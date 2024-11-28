package ca.mcmaster.cas735.acmepark.visitor_identification.adapter.amqp;

import ca.mcmaster.cas735.acmepark.common.dtos.AccessGateRequest;
import ca.mcmaster.cas735.acmepark.visitor_identification.business.entities.ParkingFeeTransaction;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.ExitLot;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.GateOpener;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.PaymentSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AMQPSender implements GateOpener, ExitLot, PaymentSender {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.payment-processing-exchange}") private String paymentExchange;
    @Value("${app.custom.messaging.gate-access-exchange}") private String gateExchange;
    @Value("${app.custom.messaging.exit-exchange}") private String exitExchange;

    private final String initiator = "visitor";

    @Autowired
    public AMQPSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void requestGateOpen(AccessGateRequest request) {
        rabbitTemplate.convertAndSend(gateExchange, "*", toJSONString(request));
    }

    @Override
    public void exitGateOpen() {
//        rabbitTemplate.convertAndSend(exitExchange, "*", null);
    }

    private String toJSONString(Object object) {
        ObjectMapper mapper= new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendTransaction(ParkingFeeTransaction transaction) {

    }
}
