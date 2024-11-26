package ca.mcmaster.cas735.acmepark.lot_management;

import ca.mcmaster.cas735.acmepark.lot_management.business.RecordManager;
import ca.mcmaster.cas735.acmepark.lot_management.business.entities.EntryRecord;
import ca.mcmaster.cas735.acmepark.lot_management.dtos.IssueVehicleFine;
import ca.mcmaster.cas735.acmepark.lot_management.port.required.EntryRecordDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@Sql(scripts = "/data.sql")
public class RecordManagerTest {
//    @Autowired
//    private RecordManager manager;
//
//    @Autowired
//    private EntryRecordDataRepository database;
//
//    @Test
//    void test_FindRecord_thenReturnUser() {
//        String license = "squeeze";
//        Integer fine = 50000;
//        IssueVehicleFine request = new IssueVehicleFine(license, fine);
//
//        manager.issueFine(request);
//
//        Optional<EntryRecord> result = database.findByLicensePlate(license);
//        assertThat(result).isPresent();
//        assertThat(result.get().getUserId()).isEqualTo(0);
//    }
}
