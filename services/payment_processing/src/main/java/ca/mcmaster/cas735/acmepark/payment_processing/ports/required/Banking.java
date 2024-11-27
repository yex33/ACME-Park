package ca.mcmaster.cas735.acmepark.payment_processing.ports.required;

import java.util.UUID;

public interface Banking {
    void reserveCredit(UUID userId, Integer amount);
}
