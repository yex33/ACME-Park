package ca.mcmaster.cas735.acmepark.lot_management.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class PrintQRcode {
    // TODO: QR code is an entity???
    private String QRcode = "QR code";
    private String license;
}
