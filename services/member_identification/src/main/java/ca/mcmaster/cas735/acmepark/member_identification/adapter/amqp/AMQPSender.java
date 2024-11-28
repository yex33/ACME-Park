package ca.mcmaster.cas735.acmepark.member_identification.adapter.amqp;

import ca.mcmaster.cas735.acmepark.common.dtos.AccessGateRequest;
import ca.mcmaster.cas735.acmepark.common.dtos.BaseTransaction;
import ca.mcmaster.cas735.acmepark.common.dtos.PaymentRequest;
import ca.mcmaster.cas735.acmepark.member_identification.business.entities.MemberFeeTransaction;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.GateManagement;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.MonitorDataSender;
import ca.mcmaster.cas735.acmepark.member_identification.ports.provided.PaymentSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AMQPSender implements PaymentSender, GateManagement, MonitorDataSender {
    final StreamBridge streamBridge;

    @Autowired
    public AMQPSender(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void sendTransaction(MemberFeeTransaction transaction) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.initiator = transaction.getInitiatedBy();
        paymentRequest.transactions = List.of(transaction).stream().map( t -> {
            BaseTransaction baseTransaction = new BaseTransaction(t.getTransactionId(), t.getTransactionType(), t.getTransactionStatus(), t.getTimestamp(), t.getAmount(), t.getInitiatedBy(), t.getUserType(), t.getDescription());
            return baseTransaction;
        }).collect(Collectors.toList());

        streamBridge.send("sendTransaction-out-0", paymentRequest);
    }

    @Override
    public void requestGateOpen(AccessGateRequest request) {
        streamBridge.send("requestGateOpen-out-0", request);
    }

    @Override
    public void sendPermitSale(String permitId) {

    }

    private String toJSONString(Object object) {
        ObjectMapper mapper= new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
