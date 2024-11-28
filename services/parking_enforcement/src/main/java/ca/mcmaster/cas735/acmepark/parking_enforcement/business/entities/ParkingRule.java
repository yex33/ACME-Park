package ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
@Entity
@Table(name = "parking_rule")
public class ParkingRule {
    @Id
    @SequenceGenerator(name = "parkingRuleSeq", sequenceName = "seq_parking_rule", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parkingRuleSeq")
    private Long parkingRuleId;

    private String name;
    private Integer fineAmount;
}
