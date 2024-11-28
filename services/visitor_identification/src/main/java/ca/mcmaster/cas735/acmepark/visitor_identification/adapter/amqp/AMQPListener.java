package ca.mcmaster.cas735.acmepark.visitor_identification.adapter.amqp;

import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.ExitLot;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.PaymentStatusHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AMQPListener implements PaymentStatusHandler {

    private final ExitLot gateManager;

    @Autowired
    public AMQPListener(ExitLot gateManager) {
        this.gateManager = gateManager;
    }

    @Override
    public void handlePaymentStatusChanged() {
        gateManager.exitGateOpen();
    }
}
