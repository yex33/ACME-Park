package ca.mcmaster.cas735.acmepark.visitor_identification.business.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="visitors")
public class Visitor {
    @Id
    private String visitorId;
    private String licensePlate;
    private String gateId;
    private LocalDateTime accessTime;
    private LocalDateTime exitTime;
    private Boolean exited;
}
