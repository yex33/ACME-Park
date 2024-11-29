package ca.mcmaster.cas735.acmepark.payment_processing.business.entities;

import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
@Entity
public class Charge {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    String transactionId;

    @Enumerated(EnumType.STRING)
    TransactionType type;

    String description;

    Integer amount;

    LocalDate issuedOn;
}
