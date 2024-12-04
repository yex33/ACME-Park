package ca.mcmaster.cas735.acmepark.lot_management.adapter.rest;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.ExitGateRequest;
import ca.mcmaster.cas735.acmepark.lot_management.port.provided.ExitGateRequestReceiver;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Member exit parking lots. (VERBS - Richardson Level: 3)")
@RequestMapping(value = "/gate")
public class MemberExitGateController {
    private final ExitGateRequestReceiver exitGateRequestReceiver;

    @Autowired
    public MemberExitGateController(ExitGateRequestReceiver exitGateRequestReceiver) {
        this.exitGateRequestReceiver = exitGateRequestReceiver;
    }

    @PostMapping("/{gate_id}/exit")
    public void handleExit(@PathVariable("gate_id") String gateId, @RequestBody ExitGateRequest request) {
        exitGateRequestReceiver.allowExit(request);
    }

}
