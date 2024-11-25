package ca.mcmaster.cas735.acmepark.parking_enforcement.adapter;

import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.Invoice;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided.ChargeEventHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@AllArgsConstructor
public class ChargeEventHandlersConfig {
    private final ChargeEventHandler chargeEventHandler;

    @Bean
    public Function<Invoice, Invoice> invoiceProcessor() {
        return chargeEventHandler::attachFines;
    }
}
