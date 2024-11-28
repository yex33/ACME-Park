package ca.mcmaster.cas735.acmepark.member_identification.adapter.amqp;

import ca.mcmaster.cas735.acmepark.common.dtos.AccessGateRequest;
import ca.mcmaster.cas735.acmepark.common.dtos.ChargeDto;
import ca.mcmaster.cas735.acmepark.common.dtos.PaymentRequest;
import ca.mcmaster.cas735.acmepark.common.dtos.User;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.MemberFeeTransaction;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.GateManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.MonitorDataSender;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.PaymentSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AMQPSender implements PaymentSender, GateManagement, MonitorDataSender {
    final StreamBridge streamBridge;

    @Autowired
    public AMQPSender(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void sendTransaction(MemberFeeTransaction memberFeeTransaction) {
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
        streamBridge.send("sendTransaction-out-0", paymentRequest);
    }

    @Override
    public void requestGateOpen(AccessGateRequest request) {
        streamBridge.send("requestGateOpen-out-0", request);
    }

    @Override
    public void sendPermitSale(String permitId) {

    }
}
