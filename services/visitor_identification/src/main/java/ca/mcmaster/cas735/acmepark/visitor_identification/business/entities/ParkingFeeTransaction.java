package ca.mcmaster.cas735.acmepark.visitor_identification.business.entities;

import ca.mcmaster.cas735.acmepark.common.dtos.BaseTransaction;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="parking_fee_transaction")
public class ParkingFeeTransaction extends BaseTransaction {
}
