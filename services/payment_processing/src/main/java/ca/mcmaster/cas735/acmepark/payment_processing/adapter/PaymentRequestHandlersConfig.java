package ca.mcmaster.cas735.acmepark.payment_processing.adapter;

import ca.mcmaster.cas735.acmepark.payment_processing.dto.InvoiceDto;
import ca.mcmaster.cas735.acmepark.payment_processing.dto.PaymentMethodSelectionRequest;
import ca.mcmaster.cas735.acmepark.payment_processing.ports.provided.PaymentRequestHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class PaymentRequestHandlersConfig {
    private final PaymentRequestHandling paymentRequestHandling;

    @Autowired
    public PaymentRequestHandlersConfig(PaymentRequestHandling paymentRequestHandling) {
        this.paymentRequestHandling = paymentRequestHandling;
    }

    @Bean
    public Function<InvoiceDto, PaymentMethodSelectionRequest> paymentRequestProcessor() {
        return paymentRequestHandling::attachAvailablePaymentMethods;
    }
}
