package ca.mcmaster.cas735.acmepark.visitor_identification.adapter.amqp;

import ca.mcmaster.cas735.acmepark.common.dtos.PaymentEvent;
import ca.mcmaster.cas735.acmepark.common.dtos.TransactionStatus;
import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.ParkingFeeManagement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class AMQPListener {

    private final ParkingFeeManagement parkingFeeManager;

    @Autowired
    public AMQPListener(ParkingFeeManagement parkingFeeManager) {
        this.parkingFeeManager = parkingFeeManager;
    }

    @Bean
    public Consumer<PaymentEvent> handlePaymentStatusChanged() {
        return (paymentEvent) -> {
            TransactionStatus status = paymentEvent.getStatus();
            if (status == TransactionStatus.SUCCESS) {
                paymentEvent.getTransactions().stream()
                        .filter(t -> t.getTransactionType().equals(TransactionType.PARKING_FEE))
                        .forEach(t -> parkingFeeManager.handleParkingFeeStatusChanged(t.getTransactionId()));
            }
        };
    }
}
