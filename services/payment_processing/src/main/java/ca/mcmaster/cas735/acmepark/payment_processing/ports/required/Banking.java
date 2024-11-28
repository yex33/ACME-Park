package ca.mcmaster.cas735.acmepark.payment_processing.ports.required;

import java.util.UUID;

public interface Banking {
    boolean reserveCredit(UUID userId, Integer amount);
}
