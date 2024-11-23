package ca.mcmaster.cas735.acmepark.lot_management.business;

import ca.mcmaster.cas735.acmepark.lot_management.port.provided.AccessGateRequestReceiver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class RuleManager implements AccessGateRequestReceiver {

    @Override
    public void checkRule(){

        // After checking the rule
    }
}
