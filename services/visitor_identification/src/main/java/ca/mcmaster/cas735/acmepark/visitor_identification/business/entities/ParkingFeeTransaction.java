package ca.mcmaster.cas735.acmepark.visitor_identification.business.entities;

import ca.mcmaster.cas735.acmepark.common.dtos.TransactionStatus;
import ca.mcmaster.cas735.acmepark.common.dtos.TransactionType;
import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="parking_fee_transaction")
public class ParkingFeeTransaction {
    @Id
    private String transactionId;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    // The timestamp when the transaction was initiated
    private LocalDateTime timestamp;

    // The amount involved in the transaction, measured in cent, e.g. 2000 is equal to 20.00 dollar
    private Integer amount;

    // ID of the user or entity initiating the transaction
    private String initiatedBy;

    // The user type of the initiator
    private UserType userType;

    // Description or purpose of the transaction
    private String description;

    private String gateId;

    private String licensePlate;

}
