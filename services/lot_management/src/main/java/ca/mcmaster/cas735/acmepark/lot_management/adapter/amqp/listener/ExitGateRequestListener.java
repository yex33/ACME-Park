package ca.mcmaster.cas735.acmepark.lot_management.adapter.amqp.listener;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.ExitGateRequest;
import ca.mcmaster.cas735.acmepark.lot_management.port.provided.ExitGateRequestReceiver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class ExitGateRequestListener {
    private final ExitGateRequestReceiver exitGateRequestReceiver;

    public ExitGateRequestListener(ExitGateRequestReceiver exitGateRequestReceiver) {
        this.exitGateRequestReceiver = exitGateRequestReceiver;
    }

    @Bean
    public Consumer<ExitGateRequest> exitGateConsumer() {
        return request -> {
            try {
                exitGateRequestReceiver.allowExit(request);
            } catch (IllegalArgumentException e) {
                log.error("Invalid message: {}", request, e);
            } catch (Exception e) {
                log.error("Unexpected error processing message: {}", request, e);
            }
        };
    }
}
