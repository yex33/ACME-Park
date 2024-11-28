package ca.mcmaster.cas735.acmepark.lot_management.port.required;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.PrintQRcode;

public interface QRcodePrintSender {
    void sendQRcodePrint(PrintQRcode qrCode);
}
