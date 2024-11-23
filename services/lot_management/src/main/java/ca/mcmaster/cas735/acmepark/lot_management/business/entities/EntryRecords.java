package ca.mcmaster.cas735.acmepark.lot_management.business.entities;

import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import jakarta.persistence.*;
import lombok.Data;

@Entity @Data @Table(name="entry_record")
public class EntryRecords {
    @Id
    private Integer recordId;

//    @Column(nullable = false, unique = true)
    private String licensePlate;
//    @Column(nullable = false, unique = true)
    private String userId;
//    @Column(nullable = false)
    private UserType userType;
}

