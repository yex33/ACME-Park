package ca.mcmaster.cas735.acmepark.member_identification.adapter.amqp;

import ca.mcmaster.cas735.acmepark.common.dtos.PaymentEvent;
import ca.mcmaster.cas735.acmepark.common.dtos.TransactionStatus;
import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.MemberFeeManagement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class AMQPListener {

    private final MemberFeeManagement memberFeeManager;

    @Autowired
    public AMQPListener(MemberFeeManagement memberFeeManager) {
        this.memberFeeManager = memberFeeManager;
    }

    @Bean
    public Consumer<PaymentEvent> handlePaymentSuccess() {
        return (paymentEvent) -> {
            log.info("Received payment event: {}", paymentEvent);
            TransactionStatus status = paymentEvent.getStatus();
            if (status == TransactionStatus.SUCCESS) {
                log.info("Payment event status is SUCCESS. Processing associated transactions.");
                paymentEvent.getTransactions().stream()
                        .filter(t -> t.getTransactionType().equals(TransactionType.MEMBER_FEE))
                        .forEach(t -> memberFeeManager.completeTransaction(t.getTransactionId()));
            } else {
                log.warn("Payment event status is not SUCCESS. Event ignored: {}", paymentEvent);
            }
        };
    }
}
