package ca.mcmaster.cas735.acmepark.payment_processing.adapter;

import ca.mcmaster.cas735.acmepark.payment_processing.ports.required.PaySlipManagement;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaySlipController implements PaySlipManagement {
    @Override
    public boolean withholdCredit(UUID userId, Integer amount) {
        // Issue synchronous call to an external payslip management service
        return true;
    }
}
