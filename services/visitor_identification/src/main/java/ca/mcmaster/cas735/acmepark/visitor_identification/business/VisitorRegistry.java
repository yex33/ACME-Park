package ca.mcmaster.cas735.acmepark.visitor_identification.business;

import ca.mcmaster.cas735.acmepark.common.dtos.AccessGateRequest;
import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import ca.mcmaster.cas735.acmepark.visitor_identification.business.entities.Visitor;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.GateManagement;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.VisitorManagement;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.VoucherManagement;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.required.VisitorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class VisitorRegistry implements VisitorManagement {

    private VisitorDataRepository database;
    private GateManagement gateManager;
    private VoucherManagement voucherManager;


    @Autowired
    public VisitorRegistry(VisitorDataRepository database, GateManagement gateManager, VoucherManagement voucherManager) {
        this.database = database;
        this.gateManager = gateManager;
        this.voucherManager = voucherManager;
    }

    @Override
    public void requestAccess(String licensePlate, String gateId) {
        // Find invalid visitor records, and delete them
        List<Visitor> invalidRecords = database.findVisitorsByLicensePlateAndExitedFalse(licensePlate);

        invalidRecords.forEach(r -> {
            database.deleteVisitorByVisitorId(r.getVisitorId());
        });

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
        if (voucherId != null) {
            voucherManager.redeemVoucher(visitorId, licensePlate);
        }
    }
}
