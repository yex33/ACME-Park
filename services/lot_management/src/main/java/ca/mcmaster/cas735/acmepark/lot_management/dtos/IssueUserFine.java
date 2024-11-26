package ca.mcmaster.cas735.acmepark.lot_management.dtos;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
public class IssueUserFine {
    private String userID;
    private String fine;
}
