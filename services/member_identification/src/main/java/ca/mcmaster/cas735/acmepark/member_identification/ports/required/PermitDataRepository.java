package ca.mcmaster.cas735.acmepark.member_identification.ports.required;

import ca.mcmaster.cas735.acmepark.member_identification.business.entities.Permit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermitDataRepository extends JpaRepository<Permit, String> {
    List<Permit> findAll();
    Boolean existsByOrganizationId(String organizationId);
}
