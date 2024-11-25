package ca.mcmaster.cas735.acmepark.parking_enforcement.dto;

import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import lombok.Value;

@Value
public class UserDto {
    Long userId;
    UserType userType;
}
