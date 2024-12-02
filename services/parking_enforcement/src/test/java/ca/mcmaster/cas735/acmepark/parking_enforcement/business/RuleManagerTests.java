package ca.mcmaster.cas735.acmepark.parking_enforcement.business;

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
import static org.assertj.core.api.Assertions.assertThatRuntimeException;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RuleManagerTests {
    @Mock
    ParkingRuleRepository repository;

    @InjectMocks
    RuleManager ruleManager;

    static String violation;
    static ParkingRule parkingRule;

    @BeforeAll
    static void setUp() {
        violation = "some parking violation";
        parkingRule = ParkingRule.builder()
                .parkingRuleId(1L)
                .name(violation)
                .fineAmount(100).build();
    }

    @Test
    void givenViolation_whenGetFineForViolating_thenReturnFine() {
        when(repository.findByName(violation)).thenReturn(Optional.of(parkingRule));
        assertThat(ruleManager.getFineForViolating(violation)).isEqualTo(parkingRule.getFineAmount());
    }

    @Test
    void givenNonExistingViolation_whenGetFineForViolating_thenThrowRuntimeException() {
        when(repository.findByName(violation)).thenReturn(Optional.of(parkingRule));
        assertThatRuntimeException().isThrownBy(() -> ruleManager.getFineForViolating("non existing" + violation));
    }

}
