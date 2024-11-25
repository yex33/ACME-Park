package ca.mcmaster.cas735.acmepark.parking_enforcement.ports.required;

import ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities.ParkingRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingRuleRepository extends JpaRepository<ParkingRule, Long> {
}