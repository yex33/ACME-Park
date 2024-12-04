package ca.mcmaster.cas735.acmepark.lot_management.business;

import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import ca.mcmaster.cas735.acmepark.lot_management.business.entities.AccessRule;
import ca.mcmaster.cas735.acmepark.lot_management.business.internal.GenerateAnalysis;
import ca.mcmaster.cas735.acmepark.lot_management.business.internal.MaintainRecord;
import ca.mcmaster.cas735.acmepark.lot_management.dtos.AccessGateRequest;
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

        log.info("Access control check: gateId={}, license={}, userId={}", gateId, license, userId);
        if (accessRule.getAllowedUsers(gateId).contains(userType)) {
            if (userType == UserType.VISITOR) {
                log.info("Visitor access: QR code generated for license {}", license);
                PrintQRcode qrCode = new PrintQRcode();
                qrCode.setLicense(license);
                qrCode.setQRcode(UUID.randomUUID().toString());
                qrSender.sendQRcodePrint(qrCode);
            }
            accessSignal.setControlSignal("Access Approval");
            log.info("Access granted: userId={}, gateId={}", userId, gateId);

            maintainer.insertRecord(license, userId, userType, gateId);
            log.info("Access record inserted for license {}", license);
        }
        else {
            accessSignal.setControlSignal("Access Deny");
            log.info("Access denied: userId={}, gateId={}", userId, gateId);
        }
        accessSignal.setGateId(gateId);
        controlGateSender.sendControlResult(accessSignal);
        log.info("Access result sent: {}", accessSignal.getControlSignal());

        updateSender.sendUpdate(analysis.generateAnalysis(accessRequest.getGateId()));
        log.info("Real-time occupancy updated for gateId {}", gateId);
    }
}
