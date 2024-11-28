package ca.mcmaster.cas735.acmepark.parking_enforcement.adapter;

import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.FineEvent;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided.FineManagement;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

@AllArgsConstructor
@Configuration
public class FineEventHandlersConfig {
    private final FineManagement fineManagement;

    @Bean
    public Consumer<FineEvent> fineEventConsumer() {
        return fineManagement::registerFine;
    }

    @Bean
    public Supplier<FineEvent> fineEventSupplier() {
        return () -> FineEvent.builder()
                .userId(UUID.randomUUID())
                .violation("you should not park")
                .issuedOn(LocalDateTime.now()).build();
    }
}
