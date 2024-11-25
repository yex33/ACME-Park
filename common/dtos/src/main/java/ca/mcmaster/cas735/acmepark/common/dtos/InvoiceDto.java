package ca.mcmaster.cas735.acmepark.common.dtos;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class InvoiceDto {
    UserDto user;
    List<ChargeDto> charges;
}
