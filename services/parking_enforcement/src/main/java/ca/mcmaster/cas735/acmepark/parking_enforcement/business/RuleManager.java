package ca.mcmaster.cas735.acmepark.parking_enforcement.business;

import ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities.ParkingRule;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided.RuleManagement;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.required.ParkingRuleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class RuleManager implements RuleManagement {
    private final ParkingRuleRepository repository;

    @Override
    public Integer fineForViolating(String violation) {
        return repository.findByName(violation)
                .map(ParkingRule::getFineAmount)
                .orElseThrow(() -> new RuntimeException("No rule found for violation: " + violation));
    }
}
