package ca.mcmaster.cas735.acmepark.lot_management.adapter;

import ca.mcmaster.cas735.acmepark.lot_management.dtos.IssueUserFine;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.IssueUserFineSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class FineSender implements IssueUserFineSender {

    @Override
    public void sendApproval(IssueUserFine approval) {

    }
}
