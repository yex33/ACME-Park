package ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided;

import ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities.FineTransaction;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.FineEvent;

import java.util.List;
import java.util.UUID;

public interface FineManagement {
    void registerFine(FineEvent fineEvent);
    List<FineTransaction> registerPendingPaymentFrom(UUID userId);
    void clearFines(List<Long> transactionIds);
    void restoreFines(List<Long> transactionIds);
}
