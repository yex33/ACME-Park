package ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided;

import ca.mcmaster.cas735.acmepark.visitor_identification.dto.ParkingFeeCreationData;

public interface ParkingFeeManagement {
    void issueParkingFee(ParkingFeeCreationData feeData);
    void parkingFeeStatusChanged();
}
