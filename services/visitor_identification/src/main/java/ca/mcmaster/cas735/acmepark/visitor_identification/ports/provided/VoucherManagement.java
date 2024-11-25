package ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided;

import ca.mcmaster.cas735.acmepark.visitor_identification.business.errors.AlreadyExistingException;

public interface VoucherManagement {
    String issueVoucher(String licensePlate) throws AlreadyExistingException;
    Boolean redeemVoucher(String voucherId, String licensePlate);
}
