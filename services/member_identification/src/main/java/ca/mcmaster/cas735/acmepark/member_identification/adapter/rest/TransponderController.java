package ca.mcmaster.cas735.acmepark.member_identification.adapter.rest;

import ca.mcmaster.cas735.acmepark.common.dtos.ParkingPermitInfo;
import ca.mcmaster.cas735.acmepark.member_identification.dto.TransponderAccessData;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.TransponderManagement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Parking Transponders Management (VERBS - Richardson Level: 3)")
@RequestMapping(value = "/api/transponders")
public class TransponderController {
    private final TransponderManagement transponderManager;

    @Autowired
    public TransponderController(TransponderManagement transponderManager) {
        this.transponderManager = transponderManager;
    }

    // PUT /api/transponders
    @PutMapping(value = "/")
    @Operation(description = "Member request entering via transponder.")
    public Boolean transponderAccess(TransponderAccessData request) {
        transponderManager.requestGateOpen(request);
        return true;
    }
}
