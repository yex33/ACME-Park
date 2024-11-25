package ca.mcmaster.cas735.acmepark.visitor_identification.ports.required;

import ca.mcmaster.cas735.acmepark.visitor_identification.business.entities.ParkingFeeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingFeeTransactionRepository extends JpaRepository<ParkingFeeTransaction, String> {

}
