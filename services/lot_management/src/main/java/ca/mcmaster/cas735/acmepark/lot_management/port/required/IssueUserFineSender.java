package ca.mcmaster.cas735.acmepark.lot_management.port.required;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.IssueUserFine;

public interface IssueUserFineSender {
    void sendApproval(IssueUserFine approval);
}
