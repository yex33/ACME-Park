package ca.mcmaster.cas735.acmepark.lot_management.port.provided;

import org.springframework.stereotype.Component;

@Component("finder")
public interface LookupUser {
    void findRecord(String license, Integer fine);
}
