package ca.mcmaster.cas735.acmepark.parking_enforcement.adapter;

import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.events.fine.FineEvent;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.events.member.MemberChargeEvent;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided.ChargeEventHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
@AllArgsConstructor
public class ChargeEventHandlersConfig {
    private final ChargeEventHandler chargeEventHandler;

    @Bean
    public Function<Flux<MemberChargeEvent>, Flux<FineEvent>> memberChargeProcessor() {
        return flux -> flux.flatMap(memberChargeEvent ->
                Mono.fromSupplier(() -> chargeEventHandler.attachFines(memberChargeEvent)));
    }
}
