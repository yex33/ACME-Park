package ca.mcmaster.cas735.acmepark.member_identification.business.entities;

import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name="permits")
public class Permit {
    @Id
    private String permitId;

    private String organizationId;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Convert(converter = StringListConverter.class)
    private List<String> licensePlates;

    private String transponderId;

    private LocalDate startDate;
    private LocalDate expiryDate;

    // The valued is true means the payment has processed and the transponder has been published
    private Boolean processed;

    public final Boolean isExpired() {
        if (expiryDate.isBefore(LocalDate.now())) {
            return true;
        } else {
            return false;
        }
    }
}
