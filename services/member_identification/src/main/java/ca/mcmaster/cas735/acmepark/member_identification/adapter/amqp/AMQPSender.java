package ca.mcmaster.cas735.acmepark.member_identification.adapter.amqp;

import ca.mcmaster.cas735.acmepark.common.dtos.AccessGateRequest;
import ca.mcmaster.cas735.acmepark.common.dtos.BaseTransaction;
import ca.mcmaster.cas735.acmepark.common.dtos.PaymentRequest;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.MemberFeeTransaction;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.GateManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.PaymentSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AMQPSender implements PaymentSender, GateManagement {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.payment-processing-exchange}") private String paymentExchange;
    @Value("${app.custom.messaging.gate-access-exchange}") private String gateExchange;

    @Autowired
    public AMQPSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendTransaction(MemberFeeTransaction transaction) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.initiator = transaction.getInitiatedBy();
        paymentRequest.transactions = List.of(transaction).stream().map( t -> {
            BaseTransaction baseTransaction = new BaseTransaction(t.getTransactionId(), t.getTransactionType(), t.getTransactionStatus(), t.getTimestamp(), t.getAmount().intValue(), t.getInitiatedBy(), t.getUserType(), t.getDescription());
            return baseTransaction;
        }).collect(Collectors.toList());

        String routingKey = "payment.request.member";

        rabbitTemplate.convertAndSend(paymentExchange, routingKey, paymentRequest.toJSONString());
    }

    @Override
    public void requestGateOpen(AccessGateRequest request) {
        rabbitTemplate.convertAndSend(gateExchange, "", toJSONString(request));
    }

    private String toJSONString(Object object) {
        ObjectMapper mapper= new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
