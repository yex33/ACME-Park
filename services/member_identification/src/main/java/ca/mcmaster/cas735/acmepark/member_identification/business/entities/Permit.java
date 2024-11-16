package ca.mcmaster.cas735.acmepark.member_identification.business.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="permits")
public class Permit {
    @Id
    private String permitId;


}
