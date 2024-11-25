package ca.mcmaster.cas735.acmepark.parking_enforcement.business;

import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.ChargeDto;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.ChargeType;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.Invoice;
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
    public Invoice attachFines(Invoice invoice) {
        return fineManagement.pendingPaymentFrom(invoice.getUser().getUserId())
                .filter(fineTransactions -> !fineTransactions.isEmpty())
                .map(fineTransactions -> {
                    var fineCharges = fineTransactions.stream().map(fineTransaction -> ChargeDto.builder()
                            .transactionId(fineTransaction.getId())
                            .chargeType(ChargeType.FINE)
                            .description("Parking Violation")
                            .amount(fineTransaction.getAmount())
                            .issuedOn(fineTransaction.getIssuedOn().toLocalDate()).build());
                    return Invoice.builder()
                            .user(invoice.getUser())
                            .charges(Stream.concat(invoice.getCharges().stream(), fineCharges).toList()).build();
                })
                .orElse(invoice);
    }
}
