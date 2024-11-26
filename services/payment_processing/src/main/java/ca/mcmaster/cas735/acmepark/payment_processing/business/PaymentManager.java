package ca.mcmaster.cas735.acmepark.payment_processing.business;

import ca.mcmaster.cas735.acmepark.payment_processing.business.entities.*;
import ca.mcmaster.cas735.acmepark.payment_processing.dto.ChargeDto;
import ca.mcmaster.cas735.acmepark.payment_processing.dto.InvoiceDto;
import ca.mcmaster.cas735.acmepark.payment_processing.ports.provided.PaymentRequestHandling;
import ca.mcmaster.cas735.acmepark.payment_processing.ports.required.InvoiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentManager implements PaymentRequestHandling {
    private final InvoiceRepository repository;

    @Autowired
    public PaymentManager(InvoiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public void handlePaymentRequest(InvoiceDto invoiceDto) {
        var invoice = Invoice.builder()
                .user(User.builder()
                        .id(invoiceDto.getUser().getUserId())
                        .userType(UserType.valueOf(invoiceDto.getUser().getUserType().toString())).build())
                .charges(invoiceDto.getCharges().stream()
                        .map(chargeDto -> Charge.builder()
                                .transactionId(chargeDto.getTransactionId())
                                .type(ChargeType.valueOf(chargeDto.getTransactionType().toString()))
                                .description(chargeDto.getDescription())
                                .amount(chargeDto.getAmount())
                                .issuedOn(chargeDto.getIssuedOn()).build()).toList())
                .total(invoiceDto.getCharges().stream()
                        .map(ChargeDto::getAmount)
                        .reduce(0, Integer::sum)).build();
        repository.save(invoice);
    }
}
