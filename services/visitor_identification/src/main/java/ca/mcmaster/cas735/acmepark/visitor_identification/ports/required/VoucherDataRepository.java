package ca.mcmaster.cas735.acmepark.visitor_identification.ports.required;

import ca.mcmaster.cas735.acmepark.visitor_identification.business.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherDataRepository extends JpaRepository<Voucher, String> {

}
