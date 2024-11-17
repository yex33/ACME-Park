package ca.mcmaster.cas735.acmepark.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingPermitInfo {
    public String permitId;
    public String organizationId;
    public UserType userType;
    public List<String> licensePlates;
    public String transponderId;
    public LocalDate startDate;
    public LocalDate expiryDate;
}
