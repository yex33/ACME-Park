package ca.mcmaster.cas735.acmepark.member_identification.adapter.amqp;

import ca.mcmaster.cas735.acmepark.common.dtos.ChargeDto;
import ca.mcmaster.cas735.acmepark.common.dtos.PaymentRequest;
import ca.mcmaster.cas735.acmepark.common.dtos.User;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.MemberFeeTransaction;
import ca.mcmaster.cas735.acmepark.member_identification.dto.AccessGateRequest;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.GateManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.MonitorDataSender;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.PaymentSender;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.TransponderSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AMQPSender implements PaymentSender, GateManagement, MonitorDataSender, TransponderSender {
    final StreamBridge streamBridge;

    @Autowired
    public AMQPSender(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void sendTransaction(MemberFeeTransaction memberFeeTransaction) {
        log.info("Preparing to send a payment transaction: {}", memberFeeTransaction);

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .user(User.builder()
                        .userId(UUID.fromString(memberFeeTransaction.getInitiatedBy()))
                        .userType(memberFeeTransaction.getUserType()).build())
                .charges(List.of(ChargeDto.builder()
                        .transactionId(memberFeeTransaction.getTransactionId())
                        .transactionType(memberFeeTransaction.getTransactionType())
                        .description(memberFeeTransaction.getDescription())
                        .amount(memberFeeTransaction.getAmount())
                        .issuedOn(memberFeeTransaction.getTimestamp().toLocalDate()).build())).build();

        log.info("Payment request created successfully: {}", paymentRequest);

        boolean isSent = streamBridge.send("sendTransaction-out-0", paymentRequest);

        if (isSent) {
            log.info("Payment transaction successfully sent: {}", paymentRequest);
        } else {
            log.error("Failed to send payment transaction: {}", paymentRequest);
        }
    }

    @Override
    public void requestGateOpen(AccessGateRequest request) {
        log.info("Sending request to open gate with details: {}", request);

        boolean isSent = streamBridge.send("requestGateOpen-out-0", request);

        if (isSent) {
            log.info("Gate open request successfully sent: {}", request);
        } else {
            log.error("Failed to send gate open request: {}", request);
        }
    }

    @Override
    public void sendPermitSale(String permitId) {
        log.info("Sending permit sale notification for permit ID: {}", permitId);

        boolean isSent = streamBridge.send("sendPermitSale-out-0", permitId);

        if (isSent) {
            log.info("Permit sale notification successfully sent for permit ID: {}", permitId);
        } else {
            log.error("Failed to send permit sale notification for permit ID: {}", permitId);
        }
    }

    @Override
    public void sendTransponder(String transponderId) {
        log.info("Sending transponder for transponder ID: {}", transponderId);

        streamBridge.send("sendTransponder-out-0", transponderId);
    }
}
