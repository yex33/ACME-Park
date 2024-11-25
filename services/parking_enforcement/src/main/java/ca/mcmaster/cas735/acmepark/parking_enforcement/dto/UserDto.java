package ca.mcmaster.cas735.acmepark.parking_enforcement.dto;

import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import lombok.Value;

import java.util.UUID;

@Value
public class UserDto {
    UUID userId;
    UserType userType;
}
