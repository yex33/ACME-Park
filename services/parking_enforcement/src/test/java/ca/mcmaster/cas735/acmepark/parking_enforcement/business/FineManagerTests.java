package ca.mcmaster.cas735.acmepark.parking_enforcement.business;

import ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities.FineTransaction;
import ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities.TransactionStatus;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.FineEvent;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided.RuleManagement;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.required.FineTransactionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FineManagerTests {
    @Mock
    FineTransactionRepository repository;

    @Mock
    RuleManagement ruleManager;

    @InjectMocks
    FineManager fineManager;

    static UUID userId;
    static String violation;
    static Integer amount;
    static FineEvent fineEvent;

    @BeforeAll
    static void setUp() {
        userId = UUID.randomUUID();
        violation = "some violation";
        amount = 100;
        fineEvent = FineEvent.builder()
                .userId(userId)
                .violation(violation)
                .issuedOn(LocalDateTime.now()).build();
    }

    @Test
    void givenParkingRule_whenRegisterFine_thenSaveFineEvent() {
        when(ruleManager.getFineForViolating(violation)).thenReturn(amount);
        ArgumentCaptor<FineTransaction> captor = ArgumentCaptor.forClass(FineTransaction.class);

        fineManager.registerFine(fineEvent);

        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getUserId()).isEqualTo(fineEvent.getUserId());
        assertThat(captor.getValue().getStatus()).isEqualTo(TransactionStatus.UNPAID);
        assertThat(captor.getValue().getIssuedOn()).isEqualTo(fineEvent.getIssuedOn());
        assertThat(captor.getValue().getAmount()).isEqualTo(amount);
    }

    @Test
    void givenUnpaidFine_whenRegisterPendingPayment_thenReturnPendingFineTransaction() {
        FineTransaction fineTransaction = FineTransaction.builder()
                .id(1L)
                .userId(userId)
                .status(TransactionStatus.UNPAID)
                .issuedOn(LocalDateTime.now())
                .amount(amount).build();
        when(repository.findByUserIdAndStatusIs(any(), any())).thenReturn(List.of(fineTransaction));

        List<FineTransaction> transactions = fineManager.registerPendingPaymentFrom(userId);

        assertThat(transactions).hasSize(1);
        assertThat(transactions.getFirst().getStatus()).isEqualTo(TransactionStatus.PENDING);
    }

    @Test
    void givenPendingFine_whenClearFines_thenSetTransactionStatusPaid() {
        FineTransaction fineTransaction = FineTransaction.builder()
                .id(1L)
                .userId(userId)
                .status(TransactionStatus.PENDING)
                .issuedOn(LocalDateTime.now())
                .amount(amount).build();
        when(repository.findById(anyLong())).thenReturn(Optional.of(fineTransaction));

        fineManager.clearFines(List.of(1L));

        assertThat(fineTransaction.getStatus()).isEqualTo(TransactionStatus.PAID);
    }

    @Test
    void givenPendingFine_whenRestoreFines_thenSetTransactionStatusUnpaid() {
        FineTransaction fineTransaction = FineTransaction.builder()
                .id(1L)
                .userId(userId)
                .status(TransactionStatus.PENDING)
                .issuedOn(LocalDateTime.now())
                .amount(amount).build();
        when(repository.findById(anyLong())).thenReturn(Optional.of(fineTransaction));

        fineManager.restoreFines(List.of(1L));

        assertThat(fineTransaction.getStatus()).isEqualTo(TransactionStatus.UNPAID);
    }
}
