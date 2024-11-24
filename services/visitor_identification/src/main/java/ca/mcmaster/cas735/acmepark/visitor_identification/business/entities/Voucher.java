package ca.mcmaster.cas735.acmepark.visitor_identification.business.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="vouchers")
public class Voucher {
    @Id
    private String voucherId;

    private String licensePlate;

    private Boolean consumed;
}
