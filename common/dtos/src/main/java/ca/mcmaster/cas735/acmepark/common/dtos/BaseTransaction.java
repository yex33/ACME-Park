package ca.mcmaster.cas735.acmepark.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseTransaction {
    public String transactionId;
    public TransactionType transactionType;
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