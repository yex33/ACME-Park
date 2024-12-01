package ca.mcmaster.cas735.acmepark.parking_enforcement.business;

import ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities.FineTransaction;
import ca.mcmaster.cas735.acmepark.parking_enforcement.dto.FineEvent;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.required.FineTransactionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FineManagerTests {
    @Mock
    FineTransactionRepository repository;

    @Mock
    RuleManager ruleManager;

    @InjectMocks
    FineManager fineManager;

    static FineEvent fineEvent;
    static String violation;
    static Integer amount;

    @BeforeAll
    static void setUp() {
        violation = "some violation";
        amount = 100;
        fineEvent = FineEvent.builder()
                .userId(UUID.randomUUID())
                .violation(violation)
                .issuedOn(LocalDateTime.now()).build();
    }

    @Test
    void givenFineEvent_whenRegisterFine_thenSaveFine() {
        when(ruleManager.getFineForViolating(violation)).thenReturn(amount);
        ArgumentCaptor<FineTransaction> captor = ArgumentCaptor.forClass(FineTransaction.class);

        fineManager.registerFine(fineEvent);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getUserId()).isEqualTo(fineEvent.getUserId());
    }
}
