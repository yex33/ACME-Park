package ca.mcmaster.cas735.acmepark.parking_enforcement.business;

import ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities.ParkingRule;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided.RuleManagement;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.required.ParkingRuleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RuleManager implements RuleManagement {
    private final ParkingRuleRepository repository;

    @Autowired
    public RuleManager(ParkingRuleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Integer getFineForViolating(String violation) {
        return repository.findByName(violation)
                .map(ParkingRule::getFineAmount)
                .orElseThrow(() -> new RuntimeException("No rule found for violation: " + violation));
    }
}
