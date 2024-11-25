package ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided;

import ca.mcmaster.cas735.acmepark.visitor_identification.business.entities.ParkingFeeTransaction;

public interface PaymentSender {
    void sendTransaction(ParkingFeeTransaction transaction);
}
