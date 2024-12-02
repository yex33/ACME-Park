package ca.mcmaster.cas735.acmepark.payment_processing.business;

import ca.mcmaster.cas735.acmepark.common.dtos.*;
import ca.mcmaster.cas735.acmepark.payment_processing.business.entities.Invoice;
import ca.mcmaster.cas735.acmepark.payment_processing.dto.PaymentMethod;
import ca.mcmaster.cas735.acmepark.payment_processing.dto.PaymentMethodSelection;
import ca.mcmaster.cas735.acmepark.payment_processing.dto.PaymentMethodSelectionRequest;
import ca.mcmaster.cas735.acmepark.payment_processing.ports.required.InvoiceRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentManagerTests {

    @Mock
    private InvoiceRepository repository;

    @InjectMocks
    private PaymentManager paymentManager;

    static UUID userId;
    static User student;
    static LocalDate issuedOn;
    static ChargeDto charge;

    @BeforeAll
    static void setUp() {
        userId = UUID.randomUUID();
        student = User.builder()
                .userId(userId)
                .userType(UserType.STUDENT).build();
        issuedOn = LocalDate.now();
        charge = ChargeDto.builder()
                .transactionId(Long.toString(1L))
                .transactionType(TransactionType.VIOLATION_FINE)
                .description("")
                .amount(100)
                .issuedOn(issuedOn).build();
    }

    @Test
    void attachAvailablePaymentMethods_savesInvoiceAndReturnsPaymentMethods() {
        // Arrange
        PaymentRequest request = PaymentRequest.builder().user(student).charges(List.of(charge)).build();

        ArgumentCaptor<Invoice> invoiceCaptor = ArgumentCaptor.forClass(Invoice.class);
        when(repository.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice savedInvoice = invocation.getArgument(0);
            savedInvoice.setId(10L); // Set an ID for the saved invoice
            return savedInvoice;
        });

        // Act
        PaymentMethodSelectionRequest result = paymentManager.attachAvailablePaymentMethods(request);

        // Assert
        verify(repository).save(invoiceCaptor.capture());
        Invoice savedInvoice = invoiceCaptor.getValue();
        assertThat(savedInvoice.getUser().getId()).isEqualTo(userId);
        assertThat(savedInvoice.getTotal()).isEqualTo(100);

        assertThat(result).isNotNull();
        assertThat(result.getInvoiceId()).isEqualTo(10L);
        assertThat(result.getPaymentMethods()).contains(PaymentMethod.UPFRONT_PAYMENT);
    }

    @Test
    void processPayment_throwsExceptionWhenInvoiceNotFound() {
        // Arrange
        when(repository.findById(10L)).thenReturn(Optional.empty());

        PaymentMethodSelection paymentMethodSelection = PaymentMethodSelection.builder()
                .invoiceId(10L)
                .paymentMethod(PaymentMethod.UPFRONT_PAYMENT)
                .build();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> paymentManager.processPayment(paymentMethodSelection));
    }
}
