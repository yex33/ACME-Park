package ca.mcmaster.cas735.acmepark.lot_management.port.provided;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.IssueVehicleFine;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Component;

@Component("finder")
public interface IssueVehicleFineReceiver {
    void findRecord(IssueVehicleFine issueRequest) throws NotFoundException;
}
