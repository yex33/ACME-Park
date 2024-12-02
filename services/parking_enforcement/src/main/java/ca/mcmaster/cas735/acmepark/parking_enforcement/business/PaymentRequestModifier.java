package ca.mcmaster.cas735.acmepark.parking_enforcement.business;

import ca.mcmaster.cas735.acmepark.common.dtos.ChargeDto;
import ca.mcmaster.cas735.acmepark.common.dtos.PaymentRequest;
import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided.ChargeEventHandler;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided.FineManagement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@Slf4j
public class PaymentRequestModifier implements ChargeEventHandler {
    private final FineManagement fineManagement;

    @Autowired
    public PaymentRequestModifier(FineManagement fineManagement) {
        this.fineManagement = fineManagement;
    }

    @Override
    public PaymentRequest attachFines(PaymentRequest paymentRequest) {
        var fineCharges = fineManagement.registerPendingPaymentFrom(paymentRequest.getUser().getUserId()).stream()
                .map(fineTransaction -> ChargeDto.builder()
                        .transactionId(fineTransaction.getId().toString())
                        .transactionType(TransactionType.VIOLATION_FINE)
                        .description("")
                        .amount(fineTransaction.getAmount())
                        .issuedOn(fineTransaction.getIssuedOn().toLocalDate()).build());
        return PaymentRequest.builder()
                .user(paymentRequest.getUser())
                .charges(Stream.concat(paymentRequest.getCharges().stream(), fineCharges).toList()).build();
    }
}
