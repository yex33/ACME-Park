package ca.mcmaster.cas735.acmepark.member_identification.business.entities;

import ca.mcmaster.cas735.acmepark.common.dtos.BaseTransaction;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="member_fee_transaction")
public class MemberFeeTransaction extends BaseTransaction {

}
