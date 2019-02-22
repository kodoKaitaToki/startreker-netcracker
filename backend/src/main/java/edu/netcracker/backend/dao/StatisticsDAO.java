package edu.netcracker.backend.dao;

import edu.netcracker.backend.message.response.CarrierStatisticsResponse;
import edu.netcracker.backend.message.response.ServiceDistributionElement;
import edu.netcracker.backend.message.response.TripDistributionElement;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface StatisticsDAO {

    List<TripDistributionElement> getTripsStatistics();
    List<ServiceDistributionElement> getServicesDistribution();
    CarrierStatisticsResponse getServiceSalesStatistics(long carrierId);
    CarrierStatisticsResponse getServiceSalesStatistics(long carrierId, LocalDate from, LocalDate to);
    CarrierStatisticsResponse getTripsSalesStatistics(long carrierId);
    CarrierStatisticsResponse getTripsSalesStatistics(long carrierId, LocalDate from, LocalDate to);
    Map<String, Double> getTroubleTicketStatistics();
    Map<String, Double> getTroubleTicketStatisticsByApprover(Long approverId);
}
