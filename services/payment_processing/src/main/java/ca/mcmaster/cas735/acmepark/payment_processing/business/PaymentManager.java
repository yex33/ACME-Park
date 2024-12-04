package ca.mcmaster.cas735.acmepark.payment_processing.business;

import ca.mcmaster.cas735.acmepark.common.dtos.*;
import ca.mcmaster.cas735.acmepark.payment_processing.business.entities.*;
import ca.mcmaster.cas735.acmepark.payment_processing.business.entities.User;
import ca.mcmaster.cas735.acmepark.payment_processing.dto.*;
import ca.mcmaster.cas735.acmepark.payment_processing.ports.provided.PaymentRequestHandling;
import ca.mcmaster.cas735.acmepark.payment_processing.ports.required.Banking;
import ca.mcmaster.cas735.acmepark.payment_processing.ports.required.InvoiceRepository;
import ca.mcmaster.cas735.acmepark.payment_processing.ports.required.PaySlipManagement;
import jakarta.transaction.Transactional;
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
    private final PaySlipManagement paySlipManagement;
    private final Banking banking;

    @Autowired
    public PaymentManager(InvoiceRepository repository, PaySlipManagement paySlipManagement, Banking banking) {
        this.repository = repository;
        this.paySlipManagement = paySlipManagement;
        this.banking = banking;
    }

    @Override
    @Transactional
    public PaymentMethodSelectionRequest attachAvailablePaymentMethods(PaymentRequest paymentRequest) {
        var invoice = Invoice.builder()
                .user(User.builder()
                        .id(paymentRequest.getUser().getUserId())
                        .userType(paymentRequest.getUser().getUserType()).build())
                .charges(paymentRequest.getCharges().stream()
                        .map(chargeDto -> Charge.builder()
                                .transactionId(chargeDto.getTransactionId())
                                .type(chargeDto.getTransactionType())
                                .description(chargeDto.getDescription())
                                .amount(chargeDto.getAmount())
                                .issuedOn(chargeDto.getIssuedOn()).build()).toList())
                .total(paymentRequest.getCharges().stream()
                        .map(ChargeDto::getAmount)
                        .reduce(0, Integer::sum)).build();
        repository.save(invoice);
        log.info("Received invoice: {}", invoice);

        return PaymentMethodSelectionRequest.builder()
                .invoice(paymentRequest)
                .invoiceId(invoice.getId())
                .paymentMethods(PAYMENT_METHODS_FOR.get(invoice.getUser().getUserType())).build();
    }

    @Override
    public PaymentEvent processPayment(PaymentMethodSelection paymentMethodSelection) {
        log.info("Processing payment: {}", paymentMethodSelection);
        var invoice = repository.findById(paymentMethodSelection.getInvoiceId())
                .orElseThrow();
        boolean success = switch (paymentMethodSelection.getPaymentMethod()) {
            case UPFRONT_PAYMENT -> banking.reserveCredit(invoice.getUser().getId(), invoice.getTotal());
            case RESERVE_IN_PAYSLIP -> paySlipManagement.withholdCredit(invoice.getUser().getId(), invoice.getTotal());
        };
        return PaymentEvent.builder()
                .status(success ? TransactionStatus.SUCCESS : TransactionStatus.FAILED)
                .transactions(invoice.getCharges().stream()
                        .map(charge -> ChargeReference.builder()
                                .transactionId(charge.getTransactionId())
                                .transactionType(charge.getType()).build())
                        .toList()).build();
    }
}
