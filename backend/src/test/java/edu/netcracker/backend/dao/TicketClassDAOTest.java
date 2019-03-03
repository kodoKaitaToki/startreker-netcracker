package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.TicketClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = "test")
public class TicketClassDAOTest {

    private static final String CREATE_TEST_DB_SCRIPT = "schema.sql";
    private static final String CREATE_DATA_SCRIPT = "data.sql";

    private TicketClassDAO ticketClassDAO;

    private DataSource dataSource;

    @Autowired
    public TicketClassDAOTest(TicketClassDAO ticketClassDAO) {
        this.ticketClassDAO = ticketClassDAO;
    }

    @Before
    public void startUp() throws SQLException {
        executeScript(CREATE_TEST_DB_SCRIPT);
        executeScript(CREATE_DATA_SCRIPT);
    }

    @Test
    public void getAllTicketClassesRelatedToCarrierTestData() {
        TicketClass ticketClass1 = new TicketClass();
        //ticketClass1.set
    }

    @Test
    public void getTicketClassByDiscount() {

    }

    @Test
    public void deleteDiscountsForTicketClasses() {

    }

    private void executeScript(String scriptName) throws SQLException {
        ScriptUtils.executeSqlScript(dataSource.getConnection(),
                new EncodedResource(new ClassPathResource(scriptName),
                        StandardCharsets.UTF_8));
    }
}
