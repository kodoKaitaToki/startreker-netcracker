package edu.netcracker.backend.service;

import edu.netcracker.backend.message.response.CarrierStatisticsResponse;
import edu.netcracker.backend.message.response.ServiceDistributionElement;
import edu.netcracker.backend.message.response.TripDistributionElement;

import java.time.LocalDate;
import java.util.List;

public interface StatisticsService {

    List<ServiceDistributionElement> getServiceStatistics();
    List<TripDistributionElement> getTripsStatistics();
    CarrierStatisticsResponse getTripsSalesStatistics(long id);
    CarrierStatisticsResponse getTripsSalesStatistics(long id, LocalDate from, LocalDate to);
    CarrierStatisticsResponse getServicesSalesStatistics(long id);
    CarrierStatisticsResponse getServicesSalesStatistics(long id, LocalDate from, LocalDate to);
    List<CarrierStatisticsResponse> getTripSalesStatisticsByWeek(long id, LocalDate from, LocalDate to);
    List<CarrierStatisticsResponse> getTripSalesStatisticsByMonth(long id, LocalDate from, LocalDate to);
    List<CarrierStatisticsResponse> getServicesSalesStatisticsByWeek(long id, LocalDate from, LocalDate to);
    List<CarrierStatisticsResponse> getServicesSalesStatisticsByMonth(long id, LocalDate from, LocalDate to);
}
