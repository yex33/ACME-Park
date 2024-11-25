package ca.mcmaster.cas735.acmepark.visitor_identification.ports.required;

import ca.mcmaster.cas735.acmepark.visitor_identification.business.entities.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitorDataRepository extends JpaRepository<Visitor, String> {
    List<Visitor> findVisitorsByLicensePlateAndExitedFalse(String licensePlate);
    void deleteVisitorByVisitorId(String visitorId);
}
