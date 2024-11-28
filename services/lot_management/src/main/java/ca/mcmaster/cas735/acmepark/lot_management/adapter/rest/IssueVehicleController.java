package ca.mcmaster.cas735.acmepark.lot_management.adapter.rest;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.IssueVehicleFine;
import ca.mcmaster.cas735.acmepark.lot_management.port.provided.IssueVehicleFineReceiver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Issue Vehicle Fine (VERBS - Richardson Level: 3)")
@RequestMapping(value = "/issue")
public class IssueVehicleController {
    private final IssueVehicleFineReceiver finder;

    @Autowired
    public IssueVehicleController(IssueVehicleFineReceiver finder) {
        this.finder = finder;
    }

    // POST /issue
    @PostMapping(value = "/")
    @Operation(description = "Issue fine to the user by license")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> lookupUser(@RequestBody IssueVehicleFine issueRequest) throws BadRequestException {
        finder.issueFine(issueRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Fine issued successfully!");
    }
}
