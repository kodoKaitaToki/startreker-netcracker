package edu.netcracker.backend.service;

import edu.netcracker.backend.message.response.CarrierStatisticsResponse;
import edu.netcracker.backend.service.impl.StatisticsService;
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

    @Test
    public void getTripsSalesStatisticsTest(){
        CarrierStatisticsResponse cres = statisticsService.getTripsSalesStatistics(21);
        CarrierStatisticsResponse expectedCres = new CarrierStatisticsResponse();
        expectedCres.setSold(17L);
        expectedCres.setRevenue(480026L);
        assertThat(cres, equalTo(expectedCres));
    }

    @Test
    public void getTripsSalesStatisticsTestWithTimeLimits(){
        CarrierStatisticsResponse cres = statisticsService.getTripsSalesStatistics(
                21,
                LocalDate.of(2015, 1, 1),
                LocalDate.of(2020, 1, 1));
        CarrierStatisticsResponse expectedCres = new CarrierStatisticsResponse();
        expectedCres.setSold(5L);
        expectedCres.setRevenue(140749L);
        assertThat(cres, equalTo(expectedCres));
    }

    @Test
    public void getServicesSalesStatisticsTest(){
        CarrierStatisticsResponse cres = statisticsService.getServicesSalesStatistics(21);
        CarrierStatisticsResponse expectedCres = new CarrierStatisticsResponse();
        expectedCres.setSold(7L);
        expectedCres.setRevenue(3156L);
        assertThat(cres, equalTo(expectedCres));
    }

    @Test
    public void getServicesSalesStatisticsTestWithTimeLimits(){
        CarrierStatisticsResponse cres = statisticsService.getServicesSalesStatistics(
                21,
                LocalDate.of(2015, 1, 1),
                LocalDate.of(2020, 1, 1));
        CarrierStatisticsResponse expectedCres = new CarrierStatisticsResponse();
        expectedCres.setSold(3L);
        expectedCres.setRevenue(1546L);
        assertThat(cres, equalTo(expectedCres));
    }
}
