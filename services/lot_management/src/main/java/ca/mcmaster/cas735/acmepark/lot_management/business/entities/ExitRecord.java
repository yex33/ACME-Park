package ca.mcmaster.cas735.acmepark.lot_management.business.entities;

import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name="exit_record")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class ExitRecord {
    @Id
    @UuidGenerator
    @Column(name = "exitRecordId", unique = true)
    private String exitRecordId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "entry_record_id", referencedColumnName = "entryRecordId")
    private EntryRecord entryRecord;

    private String licensePlate;
    private String userId;
    private UserType userType;
    private String gateId;
    private LocalDateTime exitTime;
}

