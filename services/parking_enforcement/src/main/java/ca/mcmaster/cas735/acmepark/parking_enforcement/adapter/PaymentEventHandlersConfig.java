package ca.mcmaster.cas735.acmepark.parking_enforcement.adapter;

import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.PaymentEvent;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.PaymentStatus;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided.FineManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class PaymentEventHandlersConfig {
    private final FineManagement fineManagement;

    @Autowired
    public PaymentEventHandlersConfig(FineManagement fineManagement) {
        this.fineManagement = fineManagement;
    }

    @Bean
    public Consumer<PaymentEvent> paymentEventConsumer() {
        return paymentEvent -> {
            var fineTransactionIds = paymentEvent.getTransactions().stream()
                    .filter(chargeReference -> chargeReference.getTransactionType().equals(TransactionType.VIOLATION_FINE))
                    .map(chargeReference -> Long.valueOf(chargeReference.getTransactionId()))
                    .toList();
            if (paymentEvent.getStatus().equals(PaymentStatus.SUCCESS)) {
                fineManagement.clearFines(fineTransactionIds);
            } else {
                fineManagement.restoreFines(fineTransactionIds);
            }
        };
    }
}
