package ca.mcmaster.cas735.acmepark.lot_management.port.provided;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.IssueVehicleFine;

import java.util.NoSuchElementException;

public interface IssueVehicleFineReceiver {
    void issueFine(String license, IssueVehicleFine issueRequest) throws NoSuchElementException;
}
