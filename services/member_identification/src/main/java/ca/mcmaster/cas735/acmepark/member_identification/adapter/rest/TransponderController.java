package ca.mcmaster.cas735.acmepark.member_identification.adapter.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Parking Transponders Management (VERBS - Richardson Level: 3)")
@RequestMapping(value = "/api/transponders")
public class TransponderController {

}
