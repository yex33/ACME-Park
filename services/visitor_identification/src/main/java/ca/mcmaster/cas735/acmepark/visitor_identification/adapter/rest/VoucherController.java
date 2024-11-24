package ca.mcmaster.cas735.acmepark.visitor_identification.adapter.rest;

import ca.mcmaster.cas735.acmepark.visitor_identification.dto.VoucherIssuingData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Visitor Voucher Management (VERBS - Richardson Level: 3)")
@RequestMapping(value = "/api/vouchers")
public class VoucherController {

    // POST /api/vouchers
    @PostMapping(value = "/")
    @Operation(description = "Create or renew a parking permit in the system")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(VoucherIssuingData data) {

    }
}
