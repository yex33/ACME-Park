package ca.mcmaster.cas735.acmepark.parking_enforcement.ports.provided;

import ca.mcmaster.cas735.acmepark.common.events.fine.FineEvent;
import ca.mcmaster.cas735.acmepark.common.events.member.MemberChargeEvent;

public interface ChargeEventHandler {
    FineEvent attachFines(MemberChargeEvent memberChargeEvent);
}
