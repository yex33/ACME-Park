package ca.mcmaster.cas735.acmepark.member_identification.adapter.amqp;

import ca.mcmaster.cas735.acmepark.common.dtos.PaymentRequest;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.MemberFeeTransaction;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.PaymentManagement;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AMQPSender implements PaymentManagement {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.payment-processing-exchange}") private String exchange;

    private final String initiator = "member";

    @Autowired
    public AMQPSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendTransaction(MemberFeeTransaction transaction) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.initiator = this.initiator;
        paymentRequest.transactions = List.of(transaction);

        String routingKey = "payment.request.member";

        rabbitTemplate.convertAndSend(exchange, routingKey, paymentRequest.toJSONString());
    }
}
