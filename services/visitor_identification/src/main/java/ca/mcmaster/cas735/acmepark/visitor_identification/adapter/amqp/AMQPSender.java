package ca.mcmaster.cas735.acmepark.visitor_identification.adapter.amqp;

import ca.mcmaster.cas735.acmepark.common.dtos.*;
import ca.mcmaster.cas735.acmepark.visitor_identification.business.entities.ParkingFeeTransaction;
import ca.mcmaster.cas735.acmepark.visitor_identification.dto.AccessGateRequest;
import ca.mcmaster.cas735.acmepark.visitor_identification.dto.ExitGateRequest;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.ExitLot;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.GateOpener;
import ca.mcmaster.cas735.acmepark.visitor_identification.ports.provided.PaymentSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AMQPSender implements GateOpener, ExitLot, PaymentSender {

    final StreamBridge streamBridge;

    @Autowired
    public AMQPSender(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void requestGateOpen(AccessGateRequest request) {
        streamBridge.send("requestGateOpen-out-0", request);
    }

    @Override
    public void exitGateOpen(String gateId, String license) {
        log.info("Preparing to send exit gate open request for gate ID: {} and license: {}", gateId, license);

        ExitGateRequest request = new ExitGateRequest();
        request.setGateId(gateId);
        request.setLicense(license);

        boolean isSent = streamBridge.send("exitGateOpen-out-0", request);

        if (isSent) {
            log.info("Exit gate open request successfully sent for gate ID: {} and license: {}", gateId, license);
        } else {
            log.error("Failed to send exit gate open request for gate ID: {} and license: {}", gateId, license);
        }
    }

    @Override
    public void sendTransaction(ParkingFeeTransaction transaction) {
        log.info("Preparing to send payment transaction: {}", transaction);

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .user(UserDto.builder()
                        .userId(UUID.fromString(transaction.getInitiatedBy()))
                        .userType(transaction.getUserType()).build())
                .charges(List.of(ChargeDto.builder()
                        .transactionId(transaction.getTransactionId())
                        .transactionType(transaction.getTransactionType())
                        .description(transaction.getDescription())
                        .amount(transaction.getAmount())
                        .issuedOn(transaction.getTimestamp().toLocalDate()).build())).build();

        log.info("Payment request constructed: {}", paymentRequest);

        boolean isSent = streamBridge.send("sendTransaction-out-0", paymentRequest);

        if (isSent) {
            log.info("Payment transaction successfully sent for transaction ID: {}", transaction.getTransactionId());
        } else {
            log.error("Failed to send payment transaction for transaction ID: {}", transaction.getTransactionId());
        }
    }
}
