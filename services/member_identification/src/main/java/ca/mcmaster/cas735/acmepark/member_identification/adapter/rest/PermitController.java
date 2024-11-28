package ca.mcmaster.cas735.acmepark.member_identification.adapter.rest;

import ca.mcmaster.cas735.acmepark.common.dtos.ParkingPermitInfo;
import ca.mcmaster.cas735.acmepark.member_identification.business.errors.AlreadyExistingException;
import ca.mcmaster.cas735.acmepark.member_identification.dto.PermitCreationData;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.PermitManagement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Parking Permits Management (VERBS - Richardson Level: 3)")
@RequestMapping(value = "/api/permits")
@Slf4j
public class PermitController {

    private final PermitManagement manager;

    @Autowired
    public PermitController(PermitManagement manager) {
        this.manager = manager;
    }

    // GET /api/permits
    @GetMapping(value = "/")
    @Operation(description = "Lookup for all parking permits.")
    public List<ParkingPermitInfo> listPermits() {
        return manager.findAll();
    }

    // POST /api/permits
    @PostMapping(value = "/")
    @Operation(description = "Create or renew a parking permit in the system")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody PermitCreationData data) throws AlreadyExistingException {
        log.info("Received request to create or renew a parking permit: {}", data);
        manager.create(data);
    }
}
