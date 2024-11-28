package ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
@Entity
@Table(name = "fine_transaction")
public class FineTransaction {
    @Id
    @SequenceGenerator(name = "fineTransactionSeq", sequenceName = "seq_fine_transaction", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fineTransactionSeq")
    private Long id;

    private UUID userId;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private LocalDateTime issuedOn;

    private Integer amount;
}
