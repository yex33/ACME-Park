package ca.mcmaster.cas735.acmepark.parking_enforcement.adapter;

import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.FineEvent;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided.FineManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class FineEventHandlersConfig {
    private final FineManagement fineManagement;

    @Autowired
    public FineEventHandlersConfig(FineManagement fineManagement) {
        this.fineManagement = fineManagement;
    }

    @Bean
    public Consumer<FineEvent> fineEventConsumer() {
        return fineManagement::registerFine;
    }
}
