package ca.mcmaster.cas735.acmepark.parking_enforcement;

import ca.mcmaster.cas735.acmepark.parking_enforcement.business.RuleManager;
import ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities.ParkingRule;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.required.ParkingRuleRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingEnforcementApplicationTests {
    @InjectMocks
    RuleManager ruleManager;

    @Mock
    ParkingRuleRepository parkingRuleRepository;

    static String violation;
    static ParkingRule parkingRule;

    @BeforeAll
    static void setUp() {
        violation = "you should not park";
        parkingRule = ParkingRule.builder()
                .parkingRuleId(1L)
                .name(violation)
                .fineAmount(100).build();
    }

    @Test
    void contextLoads() {
        when(parkingRuleRepository.findByName(violation)).thenReturn(Optional.of(parkingRule));
        assertThat(ruleManager.fineForViolating(violation)).isEqualTo(parkingRule.getFineAmount());
    }

}
