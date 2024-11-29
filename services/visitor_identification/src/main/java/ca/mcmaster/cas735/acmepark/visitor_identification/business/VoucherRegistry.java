package ca.mcmaster.cas735.acmepark.visitor_identification.business;

import ca.mcmaster.cas735.acmepark.visitor_identification.business.entities.Voucher;
import ca.mcmaster.cas735.acmepark.visitor_identification.business.errors.AlreadyExistingException;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.VoucherManagement;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.required.VoucherDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class VoucherRegistry implements VoucherManagement {

    private final VoucherDataRepository database;

    @Autowired
    public VoucherRegistry(VoucherDataRepository database) {
        this.database = database;
    }

    @Override
    public String issueVoucher(String licensePlate) throws AlreadyExistingException {
        log.info("Attempting to issue a voucher for license plate: {}", licensePlate);

        // Check if a voucher already exists for the license plate
        if (database.findVoucherByLicensePlateAndConsumedFalse(licensePlate) != null) {
            log.warn("Voucher already exists for license plate: {}", licensePlate);
            throw new AlreadyExistingException("Voucher", licensePlate, "licensePlate");
        }

        // Create and save a new voucher
        Voucher voucher = new Voucher();
        voucher.setVoucherId(UUID.randomUUID().toString());
        voucher.setLicensePlate(licensePlate);
        voucher.setConsumed(false);

        database.saveAndFlush(voucher);
        log.info("Voucher issued successfully with ID: {} for license plate: {}", voucher.getVoucherId(), licensePlate);

        return voucher.getVoucherId();
    }

    @Override
    public Boolean redeemVoucher(String voucherId, String licensePlate) {
        log.info("Attempting to redeem voucher with ID: {} for license plate: {}", voucherId, licensePlate);

        // Find an unconsumed voucher by license plate
        Voucher voucher = database.findVoucherByLicensePlateAndConsumedFalse(licensePlate);
        if (voucher == null) {
            log.warn("No valid voucher found for license plate: {}", licensePlate);
            return false;
        }

        // Check if the voucher matches the given license plate
        if (voucher.getLicensePlate().equals(licensePlate)) {
            voucher.setConsumed(true);
            database.saveAndFlush(voucher);
            log.info("Voucher with ID: {} successfully redeemed for license plate: {}", voucher.getVoucherId(), licensePlate);
            return true;
        } else {
            log.warn("Voucher with ID: {} does not match the provided license plate: {}", voucher.getVoucherId(), licensePlate);
            return false;
        }
    }
}
