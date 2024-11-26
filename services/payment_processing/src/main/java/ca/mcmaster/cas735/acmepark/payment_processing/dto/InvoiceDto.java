package ca.mcmaster.cas735.acmepark.payment_processing.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class InvoiceDto {
    UserDto user;
    List<ChargeDto> charges;
}
