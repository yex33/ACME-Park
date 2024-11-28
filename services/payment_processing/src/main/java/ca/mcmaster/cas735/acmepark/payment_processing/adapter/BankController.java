package ca.mcmaster.cas735.acmepark.payment_processing.adapter;

import ca.mcmaster.cas735.acmepark.payment_processing.ports.required.Banking;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BankController implements Banking {

    @Override
    public boolean reserveCredit(UUID userId, Integer amount) {
        // Issue synchronous call to an external banking service
        return true;
    }
}
