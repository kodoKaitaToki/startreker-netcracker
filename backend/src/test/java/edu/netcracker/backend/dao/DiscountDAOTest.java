package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Discount;
import org.junit.Assert;
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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest
//@AutoConfigureTestDatabase
//@ActiveProfiles(profiles = "test")
public class DiscountDAOTest {

    private static final String IF_EXIST_CONNECTION_TO_DISCOUNT_SERVICE =
            "SELECT 1 FROM discount_service WHERE discount_id = ? AND p_service_id = ?";

    private static final String IF_EXIST_CONNECTION_TO_DISCOUNT_CLASS =
            "SELECT 1 FROM discount_class WHERE discount_id = ? AND class_id = ?";

    @Autowired
    private DiscountDAO discountDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

//    @Before
//    public void beforeTest() throws SQLException, IOException {
//        executeScript("schema.sql");
//        executeScript("data.sql");
//    }

    @Test
    public void createDiscountForPossibleServiceTestWithExistingDiscount(){
        Optional<Discount> opDiscount = discountDAO.find(1);

        if (!opDiscount.isPresent()) {
            throw new RuntimeException();
        }

        Discount discount = opDiscount.get();
        discountDAO.createDiscountForPossibleService(22, discount);

        Assert.assertTrue(isExist(IF_EXIST_CONNECTION_TO_DISCOUNT_SERVICE, discount.getDiscountId(), 22));
    }

    @Test
    public void createDiscountForPossibleServiceTestWithOutExistingDiscount(){
        Discount discount = new Discount();
        discount.setStartDate(LocalDate.of(2013, 10, 14));
        discount.setFinishDate(LocalDate.of(2022, 2, 3));
        discount.setDiscountRate(125);
        discount.setDiscountType(false);

        discountDAO.createDiscountForPossibleService(22, discount);

        Assert.assertTrue(isExist(IF_EXIST_CONNECTION_TO_DISCOUNT_SERVICE, discount.getDiscountId(), 22));
    }

    @Test
    public void createDiscountForTicketClassWithExistingDiscount(){
        Optional<Discount> opDiscount = discountDAO.find(3);

        if (!opDiscount.isPresent()) {
            throw new RuntimeException();
        }

        Discount discount = opDiscount.get();
        discountDAO.createDiscountForTicketClass(2, discount);

        Assert.assertTrue(isExist(IF_EXIST_CONNECTION_TO_DISCOUNT_CLASS, discount.getDiscountId(), 2));
    }

    @Test
    public void createDiscountForTicketClassWithOutExistingDiscount(){
        Discount discount = new Discount();
        discount.setStartDate(LocalDate.of(2019, 10, 14));
        discount.setFinishDate(LocalDate.of(2032, 2, 3));
        discount.setDiscountRate(1250);
        discount.setDiscountType(false);

        discountDAO.createDiscountForTicketClass(13, discount);

        Assert.assertTrue(isExist(IF_EXIST_CONNECTION_TO_DISCOUNT_CLASS, discount.getDiscountId(), 13));
    }

    @Test
    public void getDiscountByPossibleServiceIdTest() {
        Discount discount = new Discount();
        discount.setStartDate(LocalDate.of(2012, 10, 14));
        discount.setFinishDate(LocalDate.of(2032, 2, 3));
        discount.setDiscountRate(1250);
        discount.setDiscountType(false);

        discountDAO.createDiscountForPossibleService(1, discount);

        Assert.assertTrue(isExist(IF_EXIST_CONNECTION_TO_DISCOUNT_SERVICE, discount.getDiscountId(), 1));

        Optional<Discount> opDiscount = discountDAO.getDiscountByPossibleServiceId(1);

        if (!opDiscount.isPresent()) {
            throw new RuntimeException();
        }

        Discount newDiscount = opDiscount.get();

        Assert.assertEquals(newDiscount, discount);
    }

    @Test
    public void getDiscountByTicketClassIdTest() {
        Discount discount = new Discount();
        discount.setStartDate(LocalDate.of(2011, 10, 14));
        discount.setFinishDate(LocalDate.of(2012, 2, 3));
        discount.setDiscountRate(100);
        discount.setDiscountType(false);

        discountDAO.createDiscountForTicketClass(10, discount);

        Assert.assertTrue(isExist(IF_EXIST_CONNECTION_TO_DISCOUNT_CLASS, discount.getDiscountId(), 10));

        Optional<Discount> opDiscount = discountDAO.getDiscountByTicketClassId(10);

        if (!opDiscount.isPresent()) {
            throw new RuntimeException();
        }

        Discount newDiscount = opDiscount.get();

        Assert.assertEquals(newDiscount, discount);
    }

    @Test
    public void deleteDiscountForPossibleServiceTest() {
        Discount discount = new Discount();
        discount.setStartDate(LocalDate.of(2000, 10, 14));
        discount.setFinishDate(LocalDate.of(2091, 2, 3));
        discount.setDiscountRate(100);
        discount.setDiscountType(false);

        discountDAO.createDiscountForPossibleService(15, discount);

        discountDAO.deleteDiscountForPossibleService(15);

        Assert.assertFalse(isExist(IF_EXIST_CONNECTION_TO_DISCOUNT_SERVICE, discount.getDiscountId(),15));

        Optional<Discount> opDiscount = discountDAO.find(discount.getDiscountId());

        Assert.assertNull(opDiscount.orElse(null));
    }

    @Test
    public void deleteDiscountForPossibleServiceTestWithoutDelete() {
        Discount discount = new Discount();
        discount.setStartDate(LocalDate.of(2018, 6, 17));
        discount.setFinishDate(LocalDate.of(2019, 3, 6));
        discount.setDiscountRate(19);
        discount.setDiscountType(true);

        discountDAO.createDiscountForPossibleService(15, discount);
        discountDAO.createDiscountForPossibleService(16, discount);

        discountDAO.deleteDiscountForPossibleService(15);

        Assert.assertFalse(isExist(IF_EXIST_CONNECTION_TO_DISCOUNT_SERVICE, discount.getDiscountId(),15));

        Optional<Discount> opDiscount = discountDAO.find(discount.getDiscountId());

        Assert.assertEquals(opDiscount.orElse(null), discount);
    }

    @Test
    public void createDiscountForTicketClassTest() {
        Discount discount = new Discount();
        discount.setStartDate(LocalDate.of(2000, 10, 14));
        discount.setFinishDate(LocalDate.of(2091, 2, 3));
        discount.setDiscountRate(100);
        discount.setDiscountType(false);

        discountDAO.createDiscountForTicketClass(5, discount);

        discountDAO.deleteDiscountForTicketClass(5);

        Assert.assertFalse(isExist(IF_EXIST_CONNECTION_TO_DISCOUNT_CLASS, discount.getDiscountId(),5));

        Optional<Discount> opDiscount = discountDAO.find(discount.getDiscountId());

        Assert.assertNull(opDiscount.orElse(null));
    }

    @Test
    public void createDiscountForTicketClassTestWithoutDelete() {
        Discount discount = new Discount();
        discount.setStartDate(LocalDate.of(2018, 6, 17));
        discount.setFinishDate(LocalDate.of(2019, 3, 6));
        discount.setDiscountRate(19);
        discount.setDiscountType(true);

        discountDAO.createDiscountForTicketClass(1, discount);
        discountDAO.createDiscountForTicketClass(2, discount);

        discountDAO.deleteDiscountForPossibleService(1);

        Assert.assertFalse(isExist(IF_EXIST_CONNECTION_TO_DISCOUNT_SERVICE, discount.getDiscountId(),1));

        Optional<Discount> opDiscount = discountDAO.find(discount.getDiscountId());

        Assert.assertEquals(opDiscount.orElse(null), discount);
    }

    @Test
    public void createDiscountForAllPossibleServicesForTicketClassTest() {
        Discount discount = new Discount();
        discount.setStartDate(LocalDate.of(2015, 6, 17));
        discount.setFinishDate(LocalDate.of(2020, 3, 6));
        discount.setDiscountRate(19);
        discount.setDiscountType(true);

        discountDAO.createDiscountForAllPossibleServicesForTicketClass(11, discount);
    }

    @Test
    public void test() {
        Discount discount = new Discount();
        discount.setStartDate(LocalDate.of(1000, 10, 14));
        discount.setFinishDate(LocalDate.of(2032, 2, 3));
        discount.setDiscountRate(111);
        discount.setDiscountType(true);

        discountDAO.createDiscountForTicketClass(10, discount);

        Optional<Discount> opDiscount = discountDAO.getDiscountByTicketClassId(10);

        if (!opDiscount.isPresent()) {
            throw new RuntimeException();
        }

        Discount newDiscount = opDiscount.get();

        Assert.assertEquals(newDiscount, discount);
    }

    private boolean isExist(String query, int discountId, int entityId) {
        try {
            return jdbcTemplate.queryForObject(query, new Object[] {discountId, entityId}, Integer.class) == 1;
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
