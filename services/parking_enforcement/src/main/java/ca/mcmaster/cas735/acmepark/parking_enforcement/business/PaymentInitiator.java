package ca.mcmaster.cas735.acmepark.parking_enforcement.business;

import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.ChargeDto;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.InvoiceDto;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided.ChargeEventHandler;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided.FineManagement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@AllArgsConstructor
@Service
@Slf4j
public class PaymentInitiator implements ChargeEventHandler {
    private final FineManagement fineManagement;

    @Override
    public InvoiceDto attachFines(InvoiceDto invoiceDto) {
        var fineCharges = fineManagement.registerPendingPaymentFrom(invoiceDto.getUser().getUserId())
                .stream()
                .map(fineTransaction -> ChargeDto.builder()
                        .transactionId(String.valueOf(fineTransaction.getId()))
                        .transactionType(TransactionType.VIOLATION_FINE)
                        .description("Parking Violation")
                        .amount(fineTransaction.getAmount())
                        .issuedOn(fineTransaction.getIssuedOn().toLocalDate()).build());
        return InvoiceDto.builder()
                .user(invoiceDto.getUser())
                .charges(Stream.concat(invoiceDto.getCharges().stream(), fineCharges).toList()).build();
    }
}
