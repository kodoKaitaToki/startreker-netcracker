package edu.netcracker.backend.dao;

import edu.netcracker.backend.dao.impl.DiscountDAOImpl;
import edu.netcracker.backend.model.Discount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;


@RunWith(SpringRunner.class)
@SpringBootTest
//@AutoConfigureTestDatabase
//@ActiveProfiles(profiles = "test")
public class DiscountDAOTest {

    @Autowired
    private DiscountDAO discountDAO;


    @Test
    public void daoTest(){
        Discount discount = new Discount();
        discount.setStartDate(LocalDate.of(2018, 6, 17));
        discount.setFinishDate(LocalDate.of(2019, 3, 6));
        discount.setDiscountRate(19);
        discount.setDiscountType(true);

        discountDAO.createDiscountForPossibleService(22, discount);

        //assertEquals(true, );
    }

    @Test
    public void classTicketTest(){
        Discount discount = new Discount();
        discount.setStartDate(LocalDate.of(2018, 7, 20));
        discount.setFinishDate(LocalDate.of(2019, 4, 19));
        discount.setDiscountRate(79);
        discount.setDiscountType(false);

        discountDAO.createDiscountForTicketClass(14, discount);

        //assertEquals(true, );
    }
}
