package ca.mcmaster.cas735.acmepark.payment_processing.business.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserType {
    STUDENT(ca.mcmaster.cas735.acmepark.common.dtos.UserType.STUDENT),
    STAFF(ca.mcmaster.cas735.acmepark.common.dtos.UserType.STAFF),
    FACULTY(ca.mcmaster.cas735.acmepark.common.dtos.UserType.FACULTY),
    VISITOR(ca.mcmaster.cas735.acmepark.common.dtos.UserType.VISITOR);

    private final ca.mcmaster.cas735.acmepark.common.dtos.UserType userType;
}
