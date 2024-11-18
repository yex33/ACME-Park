package ca.mcmaster.cas735.acmepark.common.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    public String initiator;
    public List<BaseTransaction> transactions;

    public String toJSONString() {
        ObjectMapper mapper= new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
