package ca.mcmaster.cas735.acmepark.parking_enforcement.business;

import ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities.FineTransaction;
import ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities.TransactionStatus;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.FineEvent;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided.FineManagement;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.required.FineTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FineManager implements FineManagement {
    private final FineTransactionRepository repository;

    @Autowired
    public FineManager(FineTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void registerFine(FineEvent fineEvent) {
        repository.save(FineTransaction.builder()
                .userId(UUID.fromString(fineEvent.getUserId()))
                .status(TransactionStatus.UNPAID)
                .issuedOn(fineEvent.getIssuedOn())
                .description(fineEvent.getDescription())
                .amount(fineEvent.getAmount()).build());
    }

    @Override
    @Transactional
    public List<FineTransaction> registerPendingPaymentFrom(UUID userId) {
        return repository.findByUserIdAndStatusIs(userId, TransactionStatus.UNPAID).stream()
                .peek(fineTransaction -> fineTransaction.setStatus(TransactionStatus.PENDING))
                .toList();
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
