package edu.netcracker.backend.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = "test")
public class SuggestionDAOTest {

    @Autowired
    private SuggestionDAO suggestionDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @Before
    public void setUp() throws Exception {
        executeScript("schema.sql");
        executeScript("data.sql");
    }

//    TODO
    @Test
    public void addPossibleServiceTest() {
    }

//    TODO
    @Test
    public void deletePossibleServiceTest() {
    }

    private boolean isExist(String query, int suggestionId, int possibleId) {
        try {
            return jdbcTemplate.queryForObject(
                    query,
                    new Object[] {suggestionId, possibleId},
                    Integer.class) == 1;
        } catch (EmptyResultDataAccessException exception) {
            return false;
        }
    }

    private void executeScript(String scriptName) throws SQLException {
        ScriptUtils.executeSqlScript(dataSource.getConnection(),
                new EncodedResource(new ClassPathResource(scriptName),
                        StandardCharsets.UTF_8));
    }
}