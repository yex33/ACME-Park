package ca.mcmaster.cas735.acmepark.lot_management.port.provided;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.IssueVehicleFine;
import jakarta.ws.rs.NotFoundException;

public interface IssueVehicleFineReceiver {
    // it should not be find record, it should be handleIssue
    void issueFine(IssueVehicleFine issueRequest) throws NotFoundException;
}
