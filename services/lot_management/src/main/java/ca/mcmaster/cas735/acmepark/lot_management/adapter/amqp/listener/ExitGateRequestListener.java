package ca.mcmaster.cas735.acmepark.lot_management.adapter.amqp.listener;

import ca.mcmaster.cas735.acmepark.common.dtos.ExitGateRequest;
import ca.mcmaster.cas735.acmepark.lot_management.port.provided.ExitGateRequestReceiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class ExitGateRequestListener {
    private final ExitGateRequestReceiver exitGateRequestReceiver;

    public ExitGateRequestListener(ExitGateRequestReceiver exitGateRequestReceiver) {
        this.exitGateRequestReceiver = exitGateRequestReceiver;
    }

    @Bean
    public Consumer<ExitGateRequest> exitGateConsumer() {
        return this.exitGateRequestReceiver::allowExit;
    }
}
