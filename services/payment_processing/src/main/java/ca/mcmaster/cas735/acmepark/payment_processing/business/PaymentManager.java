package ca.mcmaster.cas735.acmepark.payment_processing.business;

import ca.mcmaster.cas735.acmepark.payment_processing.business.entities.*;
import ca.mcmaster.cas735.acmepark.payment_processing.dto.ChargeDto;
import ca.mcmaster.cas735.acmepark.payment_processing.dto.InvoiceDto;
import ca.mcmaster.cas735.acmepark.payment_processing.dto.PaymentMethod;
import ca.mcmaster.cas735.acmepark.payment_processing.dto.PaymentMethodSelectionRequest;
import ca.mcmaster.cas735.acmepark.payment_processing.ports.provided.PaymentRequestHandling;
import ca.mcmaster.cas735.acmepark.payment_processing.ports.required.InvoiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PaymentManager implements PaymentRequestHandling {
    public static final Map<UserType, List<PaymentMethod>> PAYMENT_METHODS_FOR = Map.of(
            UserType.STUDENT, List.of(PaymentMethod.UPFRONT_PAYMENT),
            UserType.STAFF, List.of(PaymentMethod.RESERVE_IN_PAYSLIP, PaymentMethod.UPFRONT_PAYMENT),
            UserType.FACULTY, List.of(PaymentMethod.RESERVE_IN_PAYSLIP, PaymentMethod.UPFRONT_PAYMENT),
            UserType.VISITOR, List.of(PaymentMethod.UPFRONT_PAYMENT)
    );

    private final InvoiceRepository repository;

    @Autowired
    public PaymentManager(InvoiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public PaymentMethodSelectionRequest attachAvailablePaymentMethods(InvoiceDto invoiceDto) {
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

        return PaymentMethodSelectionRequest.builder()
                .invoice(invoiceDto)
                .invoiceId(invoice.getId())
                .paymentMethods(PAYMENT_METHODS_FOR.get(invoice.getUser().getUserType())).build();
    }
}
