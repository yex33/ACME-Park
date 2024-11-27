package ca.mcmaster.cas735.acmepark.lot_management.business;

import ca.mcmaster.cas735.acmepark.common.dtos.AccessGateRequest;
import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import ca.mcmaster.cas735.acmepark.lot_management.business.entities.AccessRule;
import ca.mcmaster.cas735.acmepark.lot_management.business.internal.GenerateAnalysis;
import ca.mcmaster.cas735.acmepark.lot_management.business.internal.MaintainRecord;
import ca.mcmaster.cas735.acmepark.lot_management.dtos.ControlGate;
import ca.mcmaster.cas735.acmepark.lot_management.dtos.PrintQRcode;
import ca.mcmaster.cas735.acmepark.lot_management.port.provided.AccessGateRequestReceiver;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.ControlGateSender;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.LotUpdateSender;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.QRcodePrintSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service @Slf4j
@AllArgsConstructor
public class AccessController implements AccessGateRequestReceiver {
    private static final AccessRule accessRule = new AccessRule();

    private final ControlGateSender controlGateSender;
    private final MaintainRecord maintainer;
    private final QRcodePrintSender qrSender;
    private final GenerateAnalysis analysis;
    private final LotUpdateSender updateSender;

    @Override
    public void checkRule(AccessGateRequest accessRequest){
        UserType userType = accessRequest.getUserType();
        String license = accessRequest.getLicense();
        String userId = accessRequest.getUserId();
        String gateId = accessRequest.getGateId();
        ControlGate accessSignal =  new ControlGate();

        log.info("Access control rule check");
        if (accessRule.getAllowedUsers(gateId).contains(userType)) {
            if (userType == UserType.VISITOR) {
                log.debug("Send print QR code request");
                PrintQRcode qrCode = new PrintQRcode();
                qrCode.setLicense(license);
                qrCode.setQRcode(UUID.randomUUID().toString());
                qrSender.sendQRcodePrint(qrCode);
                log.debug("QR code sent");
            }
            accessSignal.setControlSignal("Access Approval");
            log.debug("Access allowed");

            maintainer.insertRecord(license, userId, userType, gateId);
            log.debug("Record inserted");
        }
        else {
            accessSignal.setControlSignal("Access Deny");
            log.debug("Access deny");
        }
        log.info("Send access result: {}", accessSignal);
        accessSignal.setGateId(gateId);
        controlGateSender.sendControlResult(accessSignal);
        log.debug("Accessed result sent");

        log.info("Send real-time occupancy");
        updateSender.sendUpdate(analysis.generateAnalysis(accessRequest.getGateId()));
        log.debug("Update sent to dashboard");
    }
}
