package ca.mcmaster.cas735.acmepark.parking_enforcement.business;

import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.ChargeDto;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.ChargeType;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.InvoiceDto;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.events.fine.FineEvent;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.events.fine.FineStatus;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.events.member.MemberChargeEvent;
import ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities.TransactionStatus;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided.ChargeEventHandler;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.required.FineTransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class FineManagement implements ChargeEventHandler {
    private final FineTransactionRepository repository;

    @Override
    @Transactional
    public FineEvent attachFines(MemberChargeEvent memberChargeEvent) {
        var invoice = memberChargeEvent.getInvoice();
        return this.repository.findByUserIdAndStatusIs(invoice.getUser().getUserId(), TransactionStatus.UNPAID)
                .filter(fineTransactions -> !fineTransactions.isEmpty())
                .map(fineTransactions -> {
                    Stream<ChargeDto> fines = fineTransactions.stream().map(fineTransaction -> {
                        fineTransaction.setStatus(TransactionStatus.PAID);
                        return ChargeDto.builder()
                                .transactionId(fineTransaction.getId())
                                .chargeType(ChargeType.FINE)
                                .description("Parking Violation")
                                .amount(fineTransaction.getAmount())
                                .issuedOn(fineTransaction.getIssuedOn().toLocalDate()).build();
                    });
                    return FineEvent.builder()
                            .invoice(InvoiceDto.builder()
                                    .user(invoice.getUser())
                                    .charges(Stream.concat(invoice.getCharges().stream(), fines).toList()).build())
                            .fineStatus(FineStatus.FINE_CLEARED).build();
                })
                .orElse(FineEvent.builder()
                        .invoice(invoice)
                        .fineStatus(FineStatus.FINE_CLEARED).build());
    }
}
