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

    // The amount involved in the transaction, measured in cent, e.g. 2000 is equal to 20.00 dollar
    public int amount;

    // ID of the user or entity initiating the transaction
    public String initiatedBy;

    // The user type of the initiator
    public UserType userType;

    // Description or purpose of the transaction
    public String description;
}