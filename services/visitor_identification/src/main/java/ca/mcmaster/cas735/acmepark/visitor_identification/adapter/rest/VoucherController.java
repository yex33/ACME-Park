package ca.mcmaster.cas735.acmepark.visitor_identification.adapter.rest;

import ca.mcmaster.cas735.acmepark.visitor_identification.business.errors.AlreadyExistingException;
import ca.mcmaster.cas735.acmepark.visitor_identification.dto.VoucherIssuingData;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.VoucherManagement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Visitor Voucher Management (VERBS - Richardson Level: 3)")
@RequestMapping(value = "/api/vouchers")
@Slf4j
public class VoucherController {

    private final VoucherManagement voucherManager;

    @Autowired
    public VoucherController(VoucherManagement voucherManager) {
        this.voucherManager = voucherManager;
    }

    // POST /api/vouchers
    @PostMapping(value = "/")
    @Operation(description = "Create or renew a parking permit in the system")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(VoucherIssuingData data) throws AlreadyExistingException {
        log.info("Received request to issue a voucher for license plate: {}", data.getLicensePlate());
        return voucherManager.issueVoucher(data.getLicensePlate());
    }
}
