package ca.mcmaster.cas735.acmepark.common.dtos;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class PaymentRequest {
    UserDto user;
    List<ChargeDto> charges;
}
