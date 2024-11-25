package ca.mcmaster.cas735.acmepark.parking_enforcement;

import ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities.ParkingRule;
import ca.mcmaster.cas735.acmepark.parking_enforcement.ports.required.ParkingRuleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ParkingEnforcementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkingEnforcementApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ParkingRuleRepository repository) {
        return args -> repository.save(ParkingRule.builder().name("you should not park").fine_amount(100).build());
    }
}
