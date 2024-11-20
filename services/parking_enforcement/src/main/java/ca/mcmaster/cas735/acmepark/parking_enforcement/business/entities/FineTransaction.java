package ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities;

import ca.mcmaster.cas735.acmepark.common.dtos.BaseTransaction;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "fine_transaction")
public class FineTransaction extends BaseTransaction {
}
