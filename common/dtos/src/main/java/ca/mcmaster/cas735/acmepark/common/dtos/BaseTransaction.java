package ca.mcmaster.cas735.acmepark.common.dtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseTransaction {
    @Id
    public String transactionId;
    @Enumerated(EnumType.STRING)
    public TransactionType transactionType;
    @Enumerated(EnumType.STRING)
    public TransactionStatus transactionStatus;

    // The timestamp when the transaction was initiated
    public LocalDateTime timestamp;

    // The amount involved in the transaction
    public Double amount;

    // ID of the user or entity initiating the transaction
    public String initiatedBy;

    // Description or purpose of the transaction
    public String description;
}