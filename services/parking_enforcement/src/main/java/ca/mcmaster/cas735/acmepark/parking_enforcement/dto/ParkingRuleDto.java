package ca.mcmaster.cas735.acmepark.parking_enforcement.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link ca.mcmaster.cas735.acmepark.parking_enforcement.business.entities.ParkingRule}
 */
@Value
public class ParkingRuleDto implements Serializable {
    Long parkingRuleId;
    String name;
    Integer fine_amount;
}