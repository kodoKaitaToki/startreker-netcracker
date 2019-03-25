package edu.netcracker.backend.dao;

import edu.netcracker.backend.message.response.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface StatisticsDAO {

    List<TripDistributionElement> getTripsStatistics();

    List<ServiceDistributionElement> getServicesDistribution();

    CarrierRevenueResponse getServiceSalesStatistics(long carrierId);

    CarrierRevenueResponse getServiceSalesStatistics(long carrierId, LocalDate from, LocalDate to);

    CarrierRevenueResponse getTripsSalesStatistics(long carrierId);

    CarrierRevenueResponse getTripsSalesStatistics(long carrierId, LocalDate from, LocalDate to);

    ReportStatisticsResponse getTroubleTicketStatistics();

    ReportStatisticsResponse getTroubleTicketStatisticsByApprover(Long approverId);

    List<CarrierRevenueResponse> getTripsSalesStatisticsByWeek(long carrierId, LocalDate from, LocalDate to);

    List<CarrierRevenueResponse> getTripsSalesStatisticsByMonth(long carrierId, LocalDate from, LocalDate to);

    List<CarrierRevenueResponse> getServicesSalesStatisticsByWeek(long carrierId, LocalDate from, LocalDate to);

    List<CarrierRevenueResponse> getServicesSalesStatisticsByMonth(long carrierId, LocalDate from, LocalDate to);

    List<CarrierViewsResponse> getTripsViewsStatisticsByWeek(long carrierId, LocalDate from, LocalDate to);

    List<CarrierViewsResponse> getTripsViewsStatisticsByMonth(long carrierId, LocalDate from, LocalDate to);

    List<CarrierViewsResponse> getTripsViewsStatisticsByTripByWeek(long tripId, LocalDate from, LocalDate to);

    List<CarrierViewsResponse> getTripsViewsStatisticsByTripByMonth(long tripId, LocalDate from, LocalDate to);

    List<CarrierViewsResponse> getServiceViewsStatisticsByWeek(long carrierId, LocalDate from, LocalDate to);

    List<CarrierViewsResponse> getServiceViewsStatisticsByMonth(long carrierId, LocalDate from, LocalDate to);

    List<CarrierViewsResponse> getServiceViewsStatisticsByServiceByWeek(long serviceId, LocalDate from, LocalDate to);

    List<CarrierViewsResponse> getServiceViewsStatisticsByServiceByMonth(long serviceId, LocalDate from, LocalDate to);

    Map<Float, Long> getCosts(LocalDateTime from, LocalDateTime to);

    Map<Float, Long> getCostsByCarrier(Number carrierId, LocalDateTime from, LocalDateTime to);

}
