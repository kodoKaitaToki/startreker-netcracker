//package edu.netcracker.backend.service;
//
//import edu.netcracker.backend.message.response.CarrierStatisticsResponse;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.time.LocalDate;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.equalTo;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureTestDatabase
//@ActiveProfiles(profiles = "test")
//public class StatisticsServiceTest {
//
//    @Autowired
//    private StatisticsService statisticsService;
//
//    private static CarrierStatisticsResponse tripsSalesTestExpected;
//    private static CarrierStatisticsResponse tripsSalesTestWithTimeLimitsExpected;
//    private static CarrierStatisticsResponse servicesSalesTestExpected;
//    private static CarrierStatisticsResponse servicesSalesTestWithTimeLimitsExpected;
//
//    @BeforeClass
//    public static void init() {
//        tripsSalesTestExpected = new CarrierStatisticsResponse();
//        tripsSalesTestExpected.setSold(17L);
//        tripsSalesTestExpected.setRevenue(480026L);
//
//        tripsSalesTestWithTimeLimitsExpected = new CarrierStatisticsResponse();
//        tripsSalesTestWithTimeLimitsExpected.setSold(5L);
//        tripsSalesTestWithTimeLimitsExpected.setRevenue(140749L);
//        tripsSalesTestWithTimeLimitsExpected.setFrom("2015-01-01");
//        tripsSalesTestWithTimeLimitsExpected.setTo("2020-01-01");
//
//        servicesSalesTestExpected = new CarrierStatisticsResponse();
//        servicesSalesTestExpected.setSold(7L);
//        servicesSalesTestExpected.setRevenue(3156L);
//
//        servicesSalesTestWithTimeLimitsExpected = new CarrierStatisticsResponse();
//        servicesSalesTestWithTimeLimitsExpected.setSold(3L);
//        servicesSalesTestWithTimeLimitsExpected.setRevenue(1546L);
//        servicesSalesTestWithTimeLimitsExpected.setFrom("2015-01-01");
//        servicesSalesTestWithTimeLimitsExpected.setTo("2020-01-01");
//    }
//
//    @Test
//    public void getTripsSalesStatisticsTest(){
//        CarrierStatisticsResponse cres = statisticsService.getTripsSalesStatistics(21);
//        assertThat(cres, equalTo(tripsSalesTestExpected));
//    }
//
//    @Test
//    public void getTripsSalesStatisticsTestWithTimeLimits(){
//        CarrierStatisticsResponse cres = statisticsService.getTripsSalesStatistics(
//                21,
//                LocalDate.of(2015, 1, 1),
//                LocalDate.of(2020, 1, 1));
//        assertThat(cres, equalTo(tripsSalesTestWithTimeLimitsExpected));
//    }
//
//    @Test
//    public void getServicesSalesStatisticsTest(){
//        CarrierStatisticsResponse cres = statisticsService.getServicesSalesStatistics(21);
//        assertThat(cres, equalTo(servicesSalesTestExpected));
//    }
//
//    @Test
//    public void getServicesSalesStatisticsTestWithTimeLimits(){
//        CarrierStatisticsResponse cres = statisticsService.getServicesSalesStatistics(
//                21,
//                LocalDate.of(2015, 1, 1),
//                LocalDate.of(2020, 1, 1));
//        assertThat(cres, equalTo(servicesSalesTestWithTimeLimitsExpected));
//    }
//}
