package ca.mcmaster.cas735.acmepark.lot_management.port.required;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.AccessApproval;

public interface AccessApprovalSender {
    void sendApproval(AccessApproval accessApproval);
}
