package ca.mcmaster.cas735.acmepark.payment_processing.ports.required;

import ca.mcmaster.cas735.acmepark.payment_processing.business.entities.Invoice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Long> {
}
