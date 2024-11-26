package ca.mcmaster.cas735.acmepark.payment_processing.business.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
@Entity
public class User {
    @Id
    UUID id;
    UserType userType;
}
