package edu.netcracker.backend.service;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.DiscountDAO;
import edu.netcracker.backend.message.request.DiscountDTO;
import edu.netcracker.backend.model.Discount;
import edu.netcracker.backend.service.impl.DiscountServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DiscountServiceTest {

    @Mock
    private DiscountDAO discountDAO;

    @Before
    public void startUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void getDiscountDTOs() {
        Discount discount1 = new Discount();
        discount1.setDiscountId(1L);
        discount1.setIsPercent(true);
        discount1.setDiscountRate(12);
        discount1.setStartDate(LocalDateTime.of(2011, 12, 11, 3, 2));
        discount1.setFinishDate(LocalDateTime.of(2013, 12, 11, 3, 2));

        Discount discount2 = new Discount();
        discount2.setDiscountId(2L);
        discount2.setIsPercent(true);
        discount2.setDiscountRate(12);
        discount2.setStartDate(LocalDateTime.of(2010, 12, 11, 3, 2));
        discount2.setFinishDate(LocalDateTime.of(2022, 12, 11, 3, 2));

        when(discountDAO.findIn(any())).thenReturn(Arrays.asList(discount1, discount2));

        DiscountService discountService = new DiscountServiceImpl(discountDAO);

        List<DiscountDTO> discountDTOList = discountService.getDiscountDTOs(Arrays.asList(1L, 2L));

        DiscountDTO discountDTO1 = DiscountDTO.toDiscountDTO(discount1, "dd-MM-yyyy");
        discountDTO1.setIsExpired(true);
        DiscountDTO discountDTO2 = DiscountDTO.toDiscountDTO(discount2, "dd-MM-yyyy");
        discountDTO2.setIsExpired(false);

        Assert.assertEquals(discountDTOList, Arrays.asList(discountDTO1, discountDTO2));
    }

    @Test
    public void saveDiscountWithNull() {
        expectedEx.expect(RequestException.class);
        expectedEx.expectMessage("Discount is null");

        DiscountService discountService = new DiscountServiceImpl(discountDAO);

        discountService.saveDiscount(null);
    }

    @Test
    public void saveDiscountWithDateExpired() {
        expectedEx.expect(RequestException.class);
        expectedEx.expectMessage("Finish date is less than current date");

        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.setDiscountId(1L);
        discountDTO.setIsPercent(true);
        discountDTO.setDiscountRate(12);
        discountDTO.setStartDate("12-01-2013");
        discountDTO.setFinishDate("12-01-2014");

        DiscountService discountService = new DiscountServiceImpl(discountDAO);

        discountService.saveDiscount(discountDTO);
    }

    @Test
    public void saveDiscountWithDateFinishLess() {
        expectedEx.expect(RequestException.class);
        expectedEx.expectMessage("Finish date is less than start date");

        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.setDiscountId(1L);
        discountDTO.setIsPercent(true);
        discountDTO.setDiscountRate(12);
        discountDTO.setStartDate("12-01-2024");
        discountDTO.setFinishDate("12-01-2021");

        DiscountService discountService = new DiscountServiceImpl(discountDAO);

        discountService.saveDiscount(discountDTO);
    }

    @Test
    public void saveDiscountWithDiscountRateLessZero() {
        expectedEx.expect(RequestException.class);
        expectedEx.expectMessage("Discount rate must be greater than 0");

        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.setDiscountId(1L);
        discountDTO.setIsPercent(true);
        discountDTO.setDiscountRate(-123);
        discountDTO.setStartDate("12-01-2021");
        discountDTO.setFinishDate("12-01-2032");


        DiscountService discountService = new DiscountServiceImpl(discountDAO);

        discountService.saveDiscount(discountDTO);
    }

    @Test
    public void saveDiscountWithDiscountPercentGreaterThanHundred() {
        expectedEx.expect(RequestException.class);
        expectedEx.expectMessage("Discount rate in percents must be less than 100");

        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.setDiscountId(1L);
        discountDTO.setIsPercent(true);
        discountDTO.setDiscountRate(1231);
        discountDTO.setStartDate("12-01-2021");
        discountDTO.setFinishDate("12-01-2032");

        DiscountService discountService = new DiscountServiceImpl(discountDAO);

        discountService.saveDiscount(discountDTO);
    }

    @Test
    public void deleteDiscountNull() {
        expectedEx.expect(RequestException.class);
        expectedEx.expectMessage("No such discount");
        when(discountDAO.find(any())).thenReturn(Optional.empty());

        DiscountService discountService = new DiscountServiceImpl(discountDAO);
        discountService.deleteDiscount(23);
    }

    @Test
    public void deleteDiscountOverdue() {
        Discount discount = new Discount();
        discount.setDiscountId(1L);
        discount.setIsPercent(true);
        discount.setDiscountRate(12);
        discount.setStartDate(LocalDateTime.of(2011, 12, 11, 3, 2));
        discount.setFinishDate(LocalDateTime.of(2013, 12, 11, 3, 2));

        doNothing().when(discountDAO).delete(isA(Number.class));
        when(discountDAO.find(any())).thenReturn(Optional.of(discount));

        DiscountService discountService = new DiscountServiceImpl(discountDAO);
        DiscountDTO discountDTO = discountService.deleteDiscount(23);

        DiscountDTO expectedDiscountDTO = DiscountDTO.toDiscountDTO(discount, "dd-MM-yyyy");
        expectedDiscountDTO.setIsExpired(true);

        Assert.assertEquals(discountDTO, expectedDiscountDTO);
    }

    @Test
    public void deleteDiscountNotOverdue() {
        Discount discount = new Discount();
        discount.setDiscountId(1L);
        discount.setIsPercent(true);
        discount.setDiscountRate(12);
        discount.setStartDate(LocalDateTime.of(2011, 12, 11, 3, 2));
        discount.setFinishDate(LocalDateTime.of(2024, 12, 11, 3, 2));

        doNothing().when(discountDAO).delete(isA(Number.class));
        when(discountDAO.find(any())).thenReturn(Optional.of(discount));

        DiscountService discountService = new DiscountServiceImpl(discountDAO);
        DiscountDTO discountDTO = discountService.deleteDiscount(23);

        DiscountDTO expectedDiscountDTO = DiscountDTO.toDiscountDTO(discount, "dd-MM-yyyy");
        expectedDiscountDTO.setIsExpired(false);

        Assert.assertEquals(discountDTO, expectedDiscountDTO);
    }

    @Test
    public void getRelatedDiscountDTONull() {
        DiscountDTO discountDTO1 = new DiscountDTO();
        discountDTO1.setDiscountId(1L);
        discountDTO1.setIsPercent(true);
        discountDTO1.setDiscountRate(25);
        discountDTO1.setStartDate("12-01-2014");
        discountDTO1.setFinishDate("12-01-2022");

        DiscountDTO discountDTO2 = new DiscountDTO();
        discountDTO2.setDiscountId(2L);
        discountDTO2.setIsPercent(false);
        discountDTO2.setDiscountRate(1231);
        discountDTO2.setStartDate("12-01-2021");
        discountDTO2.setFinishDate("12-01-2025");

        DiscountService discountService = new DiscountServiceImpl(discountDAO);

        Assert.assertNull(discountService.getRelatedDiscountDTO(null, Arrays.asList(discountDTO1,
                discountDTO2)));
    }

    @Test
    public void getRelatedDiscountDTONoDiscounts() {
        DiscountDTO discountDTO1 = new DiscountDTO();
        discountDTO1.setDiscountId(1L);
        discountDTO1.setIsPercent(true);
        discountDTO1.setDiscountRate(25);
        discountDTO1.setStartDate("12-01-2014");
        discountDTO1.setFinishDate("12-01-2022");

        DiscountDTO discountDTO2 = new DiscountDTO();
        discountDTO2.setDiscountId(2L);
        discountDTO2.setIsPercent(false);
        discountDTO2.setDiscountRate(1231);
        discountDTO2.setStartDate("12-01-2021");
        discountDTO2.setFinishDate("12-01-2025");

        DiscountService discountService = new DiscountServiceImpl(discountDAO);

        Assert.assertEquals(discountDTO1, discountService.getRelatedDiscountDTO(1L,
                Arrays.asList(discountDTO1, discountDTO2)));
    }

    @Test
    public void getRelatedDiscountDTODiscounts() {
        DiscountDTO discountDTO1 = new DiscountDTO();
        discountDTO1.setDiscountId(1L);
        discountDTO1.setIsPercent(true);
        discountDTO1.setDiscountRate(25);
        discountDTO1.setStartDate("12-01-2014");
        discountDTO1.setFinishDate("12-01-2022");

        DiscountDTO discountDTO2 = new DiscountDTO();
        discountDTO2.setDiscountId(2L);
        discountDTO2.setIsPercent(false);
        discountDTO2.setDiscountRate(1231);
        discountDTO2.setStartDate("12-01-2021");
        discountDTO2.setFinishDate("12-01-2025");

        DiscountService discountService = new DiscountServiceImpl(discountDAO);

        Assert.assertNull(discountService.getRelatedDiscountDTO(3L,
                Arrays.asList(discountDTO1, discountDTO2)));
    }
}