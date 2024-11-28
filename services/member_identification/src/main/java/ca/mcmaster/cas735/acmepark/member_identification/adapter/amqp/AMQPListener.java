package ca.mcmaster.cas735.acmepark.member_identification.adapter.amqp;

import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.MemberFeeManagement;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AMQPListener {

    private final MemberFeeManagement memberFeeManager;

    @Autowired
    public AMQPListener(MemberFeeManagement memberFeeManager) {
        this.memberFeeManager = memberFeeManager;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${app.custom.messaging.payment-success-queue}", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.payment-processing-exchange}", type = "topic"),
            key = "*"))
    public void handlePaymentSuccess(String data) {
        this.memberFeeManager.completeTransaction(data);
    }
}
