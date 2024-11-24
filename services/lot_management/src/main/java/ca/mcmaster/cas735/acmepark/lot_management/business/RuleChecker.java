package ca.mcmaster.cas735.acmepark.lot_management.business;

import ca.mcmaster.cas735.acmepark.common.dtos.AccessGateRequest;
import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import ca.mcmaster.cas735.acmepark.lot_management.business.entities.AccessRule;
import ca.mcmaster.cas735.acmepark.lot_management.business.policies.MaintainEntryRecord;
import ca.mcmaster.cas735.acmepark.lot_management.dtos.AccessApproval;
import ca.mcmaster.cas735.acmepark.lot_management.port.provided.AccessGateRequestReceiver;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.AccessApprovalSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class RuleChecker implements AccessGateRequestReceiver {
    private static final AccessRule accessRule = new AccessRule();
    private final AccessApprovalSender sender;
    private final MaintainEntryRecord maintainer;

    @Autowired
    public RuleChecker(AccessApprovalSender sender, MaintainEntryRecord maintainer) {
        this.sender = sender;
        this.maintainer = maintainer;
    }

    @Override
    public void checkRule(AccessGateRequest accessRequest){
        UserType userType = accessRequest.getUserType();
        String license = accessRequest.getLicense();
        String userId = accessRequest.getUserId();
        String gateId = accessRequest.getGateId();
        // Get allow user and check
        log.info("Start checking the access rule");
        if (accessRule.getAllowedUsers(gateId).contains(userType)) {
            AccessApproval accessApproval =  new AccessApproval();
            switch (userType) {
                case STUDENT, STAFF, FACULTY:
                    // open gate
                    sender.sendApproval(accessApproval);
                    // policy: update entry record
                    maintainer.insertRecord(license, userId, userType);
                    log.debug("Approval sent to member.");
                    break;
                case VISITOR:
                    // print QR code
                    // open gate
                    // update entry record
                    break;
                default:
                    break;
            }
        }
        else {
            // Access Deny
        }
    }
}
