package ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "parking_rule")
public class ParkingRule {
    @Id
    public String parkingRuleId;

    public String parkingRuleName;
}
