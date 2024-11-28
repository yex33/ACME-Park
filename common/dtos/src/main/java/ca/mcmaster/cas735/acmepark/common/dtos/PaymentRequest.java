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
    // ID of the user or entity initiating the transaction
    public String initiator;

    // The user type of the initiator
    public UserType userType;

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
