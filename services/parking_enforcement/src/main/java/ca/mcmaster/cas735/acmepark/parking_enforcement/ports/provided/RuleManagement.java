package ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided;

public interface RuleManagement {
    Integer fineForViolating(String violation);
}
