package ca.mcmaster.cas735.acmepark.parking_enforcement.adapter;

import ca.mcmaster.cas735.acmepark.common.dtos.PaymentRequest;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.ChargeDto;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.InvoiceDto;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.UserDto;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided.ChargeEventHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.UUID;
import java.util.function.Function;

@AllArgsConstructor
@Configuration
public class ChargeEventHandlersConfig {
    private final ChargeEventHandler chargeEventHandler;

    @Bean
    public Function<PaymentRequest, InvoiceDto> invoiceProcessor() {
        return paymentRequest -> chargeEventHandler.attachFines(toInvoice(paymentRequest));
    }

    private InvoiceDto toInvoice(PaymentRequest paymentRequest) {
        return InvoiceDto.builder()
                .user(UserDto.builder()
                        .userId(UUID.fromString(paymentRequest.initiator))
                        .userType(paymentRequest.userType).build())
                .charges(paymentRequest.getTransactions().stream()
                        .map(baseTransaction -> ChargeDto.builder()
                                .transactionId(baseTransaction.getTransactionId())
                                .transactionType(baseTransaction.getTransactionType())
                                .description(baseTransaction.getDescription())
                                .amount(baseTransaction.getAmount())
                                .issuedOn(LocalDate.from(baseTransaction.getTimestamp())).build())
                        .toList()).build();
    }
}
