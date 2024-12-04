package ca.mcmaster.cas735.acmepark.member_identification.adapter.rest;

import ca.mcmaster.cas735.acmepark.member_identification.business.errors.NotFoundException;
import ca.mcmaster.cas735.acmepark.member_identification.dto.TransponderAccessData;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.TransponderManagement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Parking Transponders Management (VERBS - Richardson Level: 3)")
@RequestMapping(value = "/api/transponders")
@Slf4j
public class TransponderController {
    private final TransponderManagement transponderManager;

    @Autowired
    public TransponderController(TransponderManagement transponderManager) {
        this.transponderManager = transponderManager;
    }

    // PUT /api/transponders
    @PutMapping(value = "/")
    @Operation(description = "Member request entering via transponder.")
    public void transponderAccess(@RequestBody TransponderAccessData request) throws NotFoundException {
        log.info("Received transponder access request: {}", request);
        transponderManager.requestGateOpen(request);
    }
}
