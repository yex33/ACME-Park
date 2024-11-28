package ca.mcmaster.cas735.acmepark.parking_enforcement.adapter;

import ca.mcmaster.cas735.acmepark.common.dtos.PaymentRequest;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided.ChargeEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ChargeEventHandlersConfig {
    private final ChargeEventHandler chargeEventHandler;

    @Autowired
    public ChargeEventHandlersConfig(ChargeEventHandler chargeEventHandler) {
        this.chargeEventHandler = chargeEventHandler;
    }

    @Bean
    public Function<PaymentRequest, PaymentRequest> invoiceProcessor() {
        return chargeEventHandler::attachFines;
    }

}
