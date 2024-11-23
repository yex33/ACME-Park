package ca.mcmaster.cas735.acmepark.lot_management.business.entities;

import ca.mcmaster.cas735.acmepark.common.dtos.UserType;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class AccessRule {
    private final Map<String, List<UserType>> accessRule;

    // The Preset rule
    public AccessRule() {
        Map<String, List<UserType>> rules = new HashMap<>();
        rules.put("Lot M", List.of(UserType.STUDENT, UserType.STAFF));
        rules.put("Lot A", List.of(UserType.FACULTY, UserType.VISITOR));
        rules.put("Lot B", List.of(UserType.STUDENT, UserType.FACULTY));
        rules.put("Lot C", List.of(UserType.VISITOR));

        // Unmodifiable
        this.accessRule = Collections.unmodifiableMap(rules);
    }

    public List<UserType> getAllowedUsers(String lotName) {
        return accessRule.getOrDefault(lotName, List.of());
    }
}
