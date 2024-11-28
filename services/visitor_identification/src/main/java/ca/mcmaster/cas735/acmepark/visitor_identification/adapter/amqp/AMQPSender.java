package ca.mcmaster.cas735.acmepark.visitor_identification.adapter.amqp;

import ca.mcmaster.cas735.acmepark.common.dtos.*;
import ca.mcmaster.cas735.acmepark.visitor_identification.business.entities.ParkingFeeTransaction;
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
        ExitGateRequest request = new ExitGateRequest();
        request.setGateId(gateId);
        request.setLicense(license);
        streamBridge.send("exitGateOpen-out-0", request);
    }

    @Override
    public void sendTransaction(ParkingFeeTransaction transaction) {
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .user(User.builder()
                        .userId(UUID.fromString(transaction.getInitiatedBy()))
                        .userType(transaction.getUserType()).build())
                .charges(List.of(ChargeDto.builder()
                        .transactionId(transaction.getTransactionId())
                        .transactionType(transaction.getTransactionType())
                        .description(transaction.getDescription())
                        .amount(transaction.getAmount())
                        .issuedOn(transaction.getTimestamp().toLocalDate()).build())).build();

        streamBridge.send("sendTransaction-out-0", paymentRequest);
    }
}
