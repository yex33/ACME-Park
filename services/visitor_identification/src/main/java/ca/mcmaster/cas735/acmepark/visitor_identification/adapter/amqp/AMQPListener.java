package ca.mcmaster.cas735.acmepark.visitor_identification.adapter.amqp;

import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.PaymentStatusHandler;
import org.springframework.stereotype.Service;

@Service
public class AMQPListener implements PaymentStatusHandler {
    @Override
    public void handlePaymentStatusChanged() {
        
    }
}
