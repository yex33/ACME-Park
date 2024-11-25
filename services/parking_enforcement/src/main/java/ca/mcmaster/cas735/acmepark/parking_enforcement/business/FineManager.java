package ca.mcmaster.cas735.acmepark.parking_enforcement.business;

import ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities.FineTransaction;
import ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities.TransactionStatus;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.FineEvent;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided.FineManagement;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.required.FineTransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
@Slf4j
public class FineManager implements FineManagement {
    private final FineTransactionRepository repository;

    @Override
    @Transactional
    public void registerFine(FineEvent fineEvent) {
        repository.save(FineTransaction.builder()
                .userId(fineEvent.getUserId())
                .status(TransactionStatus.UNPAID)
                .issuedOn(fineEvent.getIssuedOn())
                .amount(fineEvent.getAmount()).build());
    }

    @Override
    @Transactional
    public Optional<List<FineTransaction>> pendingPaymentFrom(UUID userId) {
        return repository.findByUserIdAndStatusIs(userId, TransactionStatus.UNPAID)
                .map(fineTransactions -> {
                    fineTransactions.forEach(transaction -> transaction.setStatus(TransactionStatus.PENDING));
                    return fineTransactions;
                });
    }

    @Override
    @Transactional
    public void clearFines(List<Long> transactionIds) {
        transactionIds.forEach(id ->
                repository.findById(id).ifPresent(transaction ->
                        transaction.setStatus(TransactionStatus.PAID)));
    }

    @Override
    public void restoreFines(List<Long> transactionIds) {
        transactionIds.forEach(id ->
                repository.findById(id).ifPresent(transaction ->
                        transaction.setStatus(TransactionStatus.UNPAID)));
    }
}
