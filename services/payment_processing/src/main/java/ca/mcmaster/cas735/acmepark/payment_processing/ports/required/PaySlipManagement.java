package ca.mcmaster.cas735.acmepark.payment_processing.ports.required;

import java.util.UUID;

public interface PaySlipManagement {
    void withholdCredit(UUID userId, Integer amount);
}
