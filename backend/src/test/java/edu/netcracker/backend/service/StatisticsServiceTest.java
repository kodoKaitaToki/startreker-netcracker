package edu.netcracker.backend.service;

import edu.netcracker.backend.message.response.CarrierRevenueResponse;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = "test")
public class StatisticsServiceTest {

    @Autowired
    private StatisticsService statisticsService;

    private static CarrierRevenueResponse tripsSalesTestExpected;
    private static CarrierRevenueResponse tripsSalesTestWithTimeLimitsExpected;
    private static CarrierRevenueResponse servicesSalesTestExpected;
    private static CarrierRevenueResponse servicesSalesTestWithTimeLimitsExpected;

    @BeforeClass
    public static void init() {
        tripsSalesTestExpected = new CarrierRevenueResponse();
        tripsSalesTestExpected.setSold(45L);
        tripsSalesTestExpected.setRevenue(127128L);

        tripsSalesTestWithTimeLimitsExpected = new CarrierRevenueResponse();
        tripsSalesTestWithTimeLimitsExpected.setSold(6L);
        tripsSalesTestWithTimeLimitsExpected.setRevenue(14092L);
        tripsSalesTestWithTimeLimitsExpected.setFrom("2015-01-01");
        tripsSalesTestWithTimeLimitsExpected.setTo("2020-01-01");

        servicesSalesTestExpected = new CarrierRevenueResponse();
        servicesSalesTestExpected.setSold(9L);
        servicesSalesTestExpected.setRevenue(3144L);

        servicesSalesTestWithTimeLimitsExpected = new CarrierRevenueResponse();
        servicesSalesTestWithTimeLimitsExpected.setSold(9L);
        servicesSalesTestWithTimeLimitsExpected.setRevenue(3144L);
        servicesSalesTestWithTimeLimitsExpected.setFrom("2015-01-01");
        servicesSalesTestWithTimeLimitsExpected.setTo("2020-01-01");
    }

    @Test
    public void getTripsSalesStatisticsTest() {
        CarrierRevenueResponse cres = statisticsService.getTripsSalesStatistics(7);
        assertThat(cres, equalTo(tripsSalesTestExpected));
    }

    @Test
    public void getTripsSalesStatisticsTestWithTimeLimits() {
        CarrierRevenueResponse cres =
                statisticsService.getTripsSalesStatistics(7, LocalDate.of(2015, 1, 1), LocalDate.of(2020, 1, 1));
        assertThat(cres, equalTo(tripsSalesTestWithTimeLimitsExpected));
    }

    @Test
    public void getServicesSalesStatisticsTest() {
        CarrierRevenueResponse cres = statisticsService.getServicesSalesStatistics(7);
        assertThat(cres, equalTo(servicesSalesTestExpected));
    }

    @Test
    public void getServicesSalesStatisticsTestWithTimeLimits() {
        CarrierRevenueResponse cres =
                statisticsService.getServicesSalesStatistics(7, LocalDate.of(2015, 1, 1), LocalDate.of(2020, 1, 1));
        assertThat(cres, equalTo(servicesSalesTestWithTimeLimitsExpected));
    }
}
