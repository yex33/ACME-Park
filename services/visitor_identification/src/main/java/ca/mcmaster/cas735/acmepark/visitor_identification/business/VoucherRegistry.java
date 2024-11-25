package ca.mcmaster.cas735.acmepark.visitor_identification.business;

import ca.mcmaster.cas735.acmepark.visitor_identification.business.entities.Voucher;
import ca.mcmaster.cas735.acmepark.visitor_identification.business.errors.AlreadyExistingException;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.VoucherManagement;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.required.VoucherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VoucherRegistry implements VoucherManagement {

    private final VoucherDataRepository database;

    @Autowired
    public VoucherRegistry(VoucherDataRepository database) {
        this.database = database;
    }

    @Override
    public String issueVoucher(String licensePlate) throws AlreadyExistingException {

        if (database.findVoucherByLicensePlateAndConsumedFalse(licensePlate) != null) {
            throw new AlreadyExistingException("Voucher", licensePlate, "licensePlate");
        }

        Voucher voucher = new Voucher();
        voucher.setVoucherId(UUID.randomUUID().toString());
        voucher.setLicensePlate(licensePlate);
        voucher.setConsumed(false);

        database.saveAndFlush(voucher);

        return voucher.getVoucherId();
    }

    @Override
    public Boolean redeemVoucher(String voucherId, String licensePlate) {
        Voucher voucher = database.findVoucherByLicensePlateAndConsumedFalse(licensePlate);

        if (voucher == null) {
            return false;
        }

        if (voucher.getLicensePlate().equals(licensePlate)) {
            voucher.setConsumed(true);
            database.saveAndFlush(voucher);
            return true;
        } else {
            return false;
        }
    }
}
