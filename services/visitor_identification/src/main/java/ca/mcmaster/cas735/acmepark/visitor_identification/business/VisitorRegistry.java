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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
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
        // Find invalid visitor records, and delete them
        List<Visitor> invalidRecords = database.findVisitorsByLicensePlateAndExitedFalse(licensePlate);

        invalidRecords.forEach(r -> database.deleteVisitorByVisitorId(r.getVisitorId()));

        Visitor visitor = new Visitor();
        visitor.setVisitorId(UUID.randomUUID().toString());
        visitor.setLicensePlate(licensePlate);
        visitor.setGateId(gateId);
        visitor.setAccessTime(LocalDateTime.now());
        visitor.setExited(false);

        database.saveAndFlush(visitor);

        AccessGateRequest accessGateRequest = new AccessGateRequest();
        accessGateRequest.setUserId(visitor.getVisitorId());
        accessGateRequest.setGateId(gateId);
        accessGateRequest.setUserType(UserType.VISITOR);

        gateManager.requestGateOpen(accessGateRequest);
    }

    @Override
    public void exit(String visitorId, String licensePlate, String voucherId) {
        int parkingFee = 0;

        Visitor visitor = database.findVisitorByVisitorId(visitorId);

        if (voucherId != null) {
            voucherManager.redeemVoucher(visitorId, licensePlate);
        } else {
            Duration parkingDuration = Duration.between(visitor.getAccessTime(), LocalDateTime.now());

            long hours = parkingDuration.toHours();
            if (parkingDuration.toMinutes() % 60 != 0) {
                hours++;
            }

            parkingFee = Math.toIntExact(hours) * HOURLY_PARKING_FEE;
        }

        visitor.setExitTime(LocalDateTime.now());
        visitor.setExited(true);

        database.saveAndFlush(visitor);

        ParkingFeeCreationData feeData = new ParkingFeeCreationData();
        feeData.setVisitorId(visitorId);
        feeData.setTimestamp(LocalDateTime.now());
        feeData.setAmount(parkingFee);

        parkingFeeManager.issueParkingFee(feeData);
    }
}
