package ca.mcmaster.cas735.acmepark.common.dtos;

import lombok.Value;

@Value
public class MemberChargeDto {
    UserDto user;
    ChargeDto charge;
}
