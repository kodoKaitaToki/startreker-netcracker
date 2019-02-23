package edu.netcracker.backend.dao;

import edu.netcracker.backend.message.response.ServiceDistributionElement;
import edu.netcracker.backend.message.response.TripDistributionElement;

import java.util.List;
import java.util.Map;

public interface StatisticsDAO {
    List<TripDistributionElement> getTripsStatistics();
    List<ServiceDistributionElement> getServicesDistribution();
    Map<String, Double> getTroubleTicketStatistics();
    Map<String, Double> getTroubleTicketStatisticsByApprover(Long approverId);
}
