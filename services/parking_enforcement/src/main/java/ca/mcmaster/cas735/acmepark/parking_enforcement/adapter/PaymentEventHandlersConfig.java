package ca.mcmaster.cas735.acmepark.parking_enforcement.adapter;

import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.ChargeTransaction;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.ChargeType;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.PaymentEvent;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.PaymentStatus;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided.FineManagement;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@AllArgsConstructor
@Configuration
public class PaymentEventHandlersConfig {
    private final FineManagement fineManagement;

    @Bean
    public Consumer<PaymentEvent> paymentEventConsumer() {
        return paymentEvent -> {
            var fineTransactionIds = paymentEvent.getTransactions().stream()
                    .filter(chargeTransaction -> chargeTransaction.getChargeType().equals(ChargeType.FINE))
                    .map(ChargeTransaction::getTransactionId)
                    .toList();
            if (paymentEvent.getStatus().equals(PaymentStatus.SUCCESS)) {
                fineManagement.clearFines(fineTransactionIds);
            } else {
                fineManagement.restoreFines(fineTransactionIds);
            }
        };
    }
}
