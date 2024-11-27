package ca.mcmaster.cas735.acmepark.lot_management.business.entities;

import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name="entry_record")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class EntryRecord {
    @Id
    @UuidGenerator
    @Column(name = "entryRecordId", unique = true)
    private String entryRecordId;

    @OneToOne(mappedBy = "entryRecord", cascade = CascadeType.ALL)
    private ExitRecord exitRecord;

    private String licensePlate;
    private String userId;
    private UserType userType;
    private String gateId;
    private LocalDateTime entryTime;
}

