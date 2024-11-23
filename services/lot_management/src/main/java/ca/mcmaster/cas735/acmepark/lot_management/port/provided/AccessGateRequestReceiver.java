package ca.mcmaster.cas735.acmepark.lot_management.port.provided;

import org.springframework.stereotype.Component;

@Component(value = "decider")
public interface AccessGateRequestReceiver {
    void checkRule();
}
