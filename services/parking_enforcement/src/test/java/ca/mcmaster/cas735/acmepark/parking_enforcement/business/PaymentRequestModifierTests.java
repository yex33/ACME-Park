package ca.mcmaster.cas735.acmepark.parking_enforcement.business;

import ca.mcmaster.cas735.acmepark.common.dtos.*;
import ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities.FineTransaction;
import ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities.TransactionStatus;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided.FineManagement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentRequestModifierTests {
    @Mock
    FineManagement fineManager;

    @InjectMocks
    PaymentRequestModifier paymentRequestModifier;

    static UUID userId;
    static String violation;
    static Integer amount;
    static LocalDateTime issuedOn;
    static ChargeDto originalCharge;
    static PaymentRequest originalPaymentRequest;

    @BeforeAll
    static void setup() {
        userId = UUID.randomUUID();
        violation = "some parking violation";
        amount = 100;
        issuedOn = LocalDateTime.now();

        originalCharge = ChargeDto.builder()
                .transactionId(UUID.randomUUID().toString())
                .transactionType(TransactionType.MEMBER_FEE)
                .description("Member fee")
                .amount(500)
                .issuedOn(LocalDate.now()).build();
        originalPaymentRequest = PaymentRequest.builder()
                .user(User.builder()
                        .userId(userId)
                        .userType(UserType.STUDENT).build())
                .charges(List.of(originalCharge)).build();
    }

    @Test
    void givenPendingFineTransactions_whenAttachFines_thenReturnsModifiedPaymentRequest() {
        FineTransaction fineTransaction = FineTransaction.builder()
                .id(1L)
                .userId(userId)
                .status(TransactionStatus.PENDING)
                .issuedOn(issuedOn)
                .amount(amount).build();
        when(fineManager.registerPendingPaymentFrom(any())).thenReturn(List.of(fineTransaction));
        ChargeDto newCharge = ChargeDto.builder()
                .transactionId(Long.toString(1L))
                .transactionType(TransactionType.VIOLATION_FINE)
                .description("")
                .amount(amount)
                .issuedOn(issuedOn.toLocalDate()).build();

        PaymentRequest modified = paymentRequestModifier.attachFines(originalPaymentRequest);

        assertThat(modified.getUser()).isEqualTo(originalPaymentRequest.getUser());
        assertThat(modified.getCharges()).contains(originalCharge);
        assertThat(modified.getCharges()).contains(newCharge);
    }
}
