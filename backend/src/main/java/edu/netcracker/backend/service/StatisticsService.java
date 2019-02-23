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
}
