package ca.mcmaster.cas735.acmepark.parking_enforcement.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Invoice {
    UserDto user;
    List<ChargeDto> charges;
}
