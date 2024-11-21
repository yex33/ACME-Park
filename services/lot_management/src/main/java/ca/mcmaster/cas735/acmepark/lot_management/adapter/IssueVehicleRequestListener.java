package ca.mcmaster.cas735.acmepark.lot_management.adapter;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.IssueVehicleFineRequest;
import ca.mcmaster.cas735.acmepark.lot_management.port.provided.LookupUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class IssueVehicleRequestListener {
    private final LookupUser finder;

    @Autowired
    public IssueVehicleRequestListener(LookupUser finder) {
        this.finder = finder;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "user.issue.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.inbound-exchange-topic-lookup}",
                    ignoreDeclarationExceptions = "true", type = "topic"),
            key = "*"))
    public void listen(String data) {
        log.debug("Receiving license plate and fine amount {}", data);
        IssueVehicleFineRequest request = translate(data);
        finder.findRecord(request.getLicensePlate(), request.getFine());
    }

    private IssueVehicleFineRequest translate (String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            return mapper.readValue(raw, IssueVehicleFineRequest.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
