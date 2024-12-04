package ca.mcmaster.cas735.acmepark.lot_management.adapter.rest;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.IssueVehicleFine;
import ca.mcmaster.cas735.acmepark.lot_management.port.provided.IssueVehicleFineReceiver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@Tag(name = "Issue Vehicle Fine (VERBS - Richardson Level: 3)")
@RequestMapping(value = "/issue")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IssueVehicleController {
    private final IssueVehicleFineReceiver finder;

    @Autowired
    public IssueVehicleController(IssueVehicleFineReceiver finder) {
        this.finder = finder;
    }

    // POST /issue
    @PostMapping(value = "/{license}")
    @Operation(description = "Issue fine to the user by license")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void lookupUser(
            @PathVariable String license,
            @RequestBody IssueVehicleFine issueRequest) {
        try {
            finder.issueFine(license, issueRequest);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
