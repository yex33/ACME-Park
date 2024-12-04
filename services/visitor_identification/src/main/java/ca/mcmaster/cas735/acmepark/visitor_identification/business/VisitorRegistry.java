package ca.mcmaster.cas735.acmepark.visitor_identification.business;

import ca.mcmaster.cas735.acmepark.common.dtos.AccessGateRequest;
import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import ca.mcmaster.cas735.acmepark.visitor_identification.business.entities.Visitor;
import ca.mcmaster.cas735.acmepark.visitor_identification.dto.ParkingFeeCreationData;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.GateOpener;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.ParkingFeeManagement;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.VisitorManagement;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.VoucherManagement;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.required.VisitorDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class VisitorRegistry implements VisitorManagement {

    private final VisitorDataRepository database;
    private final GateOpener gateManager;
    private final VoucherManagement voucherManager;
    private final ParkingFeeManagement parkingFeeManager;

    // Hourly parking fee is $10.00
    private static final Integer HOURLY_PARKING_FEE = 1000;

    @Autowired
    public VisitorRegistry(VisitorDataRepository database, GateOpener gateManager, VoucherManagement voucherManager, ParkingFeeManagement parkingFeeManager) {
        this.database = database;
        this.gateManager = gateManager;
        this.voucherManager = voucherManager;
        this.parkingFeeManager = parkingFeeManager;
    }

    @Override
    public void requestAccess(String licensePlate, String gateId) {
        log.info("Processing access request for license plate: {} at gate: {}", licensePlate, gateId);

        // Find invalid visitor records, and delete them
        List<Visitor> invalidRecords = database.findVisitorsByLicensePlateAndExitedFalse(licensePlate);
        if (!invalidRecords.isEmpty()) {
            log.warn("Found {} invalid visitor records for license plate: {}. Deleting them.", invalidRecords.size(), licensePlate);
            invalidRecords.forEach(r -> {
                log.info("Deleting invalid visitor record with ID: {}", r.getVisitorId());
                database.deleteVisitorByVisitorId(r.getVisitorId());
            });
        }

        // Create a new visitor record
        Visitor visitor = new Visitor();
        visitor.setVisitorId(UUID.randomUUID().toString());
        visitor.setLicensePlate(licensePlate);
        visitor.setGateId(gateId);
        visitor.setAccessTime(LocalDateTime.now());
        visitor.setExited(false);

        database.saveAndFlush(visitor);
        log.info("New visitor record created: {}", visitor);

        // Create and send gate access request
        AccessGateRequest accessGateRequest = new AccessGateRequest();
        accessGateRequest.setUserId(visitor.getVisitorId());
        accessGateRequest.setGateId(gateId);
        accessGateRequest.setLicense(licensePlate);
        accessGateRequest.setUserType(UserType.VISITOR);

        log.info("Sending gate access request for visitor ID: {} at gate: {}", visitor.getVisitorId(), gateId);
        gateManager.requestGateOpen(accessGateRequest);
        log.info("Gate access request sent successfully for visitor ID: {}", visitor.getVisitorId());
    }

    @Override
    public void exit(String visitorId, String licensePlate, String voucherId) {
        log.info("Processing exit request for visitor ID: {}, license plate: {}, voucher ID: {}", visitorId, licensePlate, voucherId);

        int parkingFee = 0;

        Visitor visitor = database.findVisitorByVisitorId(visitorId);
        if (visitor == null) {
            log.error("Visitor not found with ID: {}", visitorId);
            throw new IllegalArgumentException("Visitor not found.");
        }
        log.info("Found visitor record: {}", visitor);

        if (voucherId != null) {
            // Redeem voucher
            log.info("Redeeming voucher for visitor ID: {}, license plate: {}, voucherId: {}", visitorId, licensePlate, voucherId);
            voucherManager.redeemVoucher(voucherId, licensePlate);
        } else {
            Duration parkingDuration = Duration.between(visitor.getAccessTime(), LocalDateTime.now());
            long hours = parkingDuration.toHours();
            if (parkingDuration.toMinutes() % 60 != 0) {
                hours++;
            }
            parkingFee = Math.toIntExact(hours) * HOURLY_PARKING_FEE;
            log.info("Parking duration: {} hours. Calculated parking fee: {}", hours, parkingFee);
        }

        // Update visitor record with exit details
        visitor.setExitTime(LocalDateTime.now());
        visitor.setExited(true);
        database.saveAndFlush(visitor);
        log.info("Visitor record updated with exit details: {}", visitor);

        // Create parking fee record
        ParkingFeeCreationData feeData = new ParkingFeeCreationData();
        feeData.setVisitorId(visitorId);
        feeData.setTimestamp(LocalDateTime.now());
        feeData.setAmount(parkingFee);
        feeData.setGateId(visitor.getGateId());
        feeData.setLicensePlate(visitor.getLicensePlate());

        parkingFeeManager.issueParkingFee(feeData);
        log.info("Parking fee issued: {}", feeData);
    }
}
