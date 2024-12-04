package ca.mcmaster.cas735.acmepark.lot_management.adapter.amqp.listener;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.AccessGateRequest;
import ca.mcmaster.cas735.acmepark.lot_management.port.provided.AccessGateRequestReceiver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class AccessGateRequestListener {
    private final AccessGateRequestReceiver accessGateRequestReceiver;

    @Autowired
    public AccessGateRequestListener(AccessGateRequestReceiver accessGateRequestReceiver) {
        this.accessGateRequestReceiver = accessGateRequestReceiver;
    }

    @Bean
    public Consumer<AccessGateRequest> accessGateRequestConsumer() {
        return request -> {
            try {
                accessGateRequestReceiver.checkRule(request);
                log.info("Access gate request: {}", request);
            } catch (IllegalArgumentException e) {
                log.error("Invalid message: {}", request);
            } catch (Exception e) {
                log.error("Unexpected error processing message: {}", request, e);
            }
        };
    }
}
