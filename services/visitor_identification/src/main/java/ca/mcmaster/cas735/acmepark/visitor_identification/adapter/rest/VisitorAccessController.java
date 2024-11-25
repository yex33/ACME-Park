package ca.mcmaster.cas735.acmepark.visitor_identification.adapter.rest;

import ca.mcmaster.cas735.acmepark.visitor_identification.dto.VisitorAccessData;
import ca.mcmaster.cas735.acmepark.visitor_identification.dto.VisitorExitData;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.VisitorManagement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Visitor Access Management (VERBS - Richardson Level: 3)")
@RequestMapping(value = "/api/visitors")
public class VisitorAccessController {

    private VisitorManagement visitorManager;

    @Autowired
    public VisitorAccessController(VisitorManagement visitorManager) {
        this.visitorManager = visitorManager;
    }

    // POST /api/visitors
    @PostMapping(value = "/")
    @Operation(description = "Request visitor access in parking lots.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void access(VisitorAccessData data) {
        visitorManager.requestAccess(data.getLicensePlate(), data.getGateId());
    }

    // PUT /api/visitors
    @PutMapping(value = "/")
    @Operation(description = "Request visitor exit from parking lots.")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void exit(VisitorExitData data) {
        visitorManager.exit(data.getVisitorId(), data.getLicensePlate(), data.getVoucherId());
    }
}
