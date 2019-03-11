package edu.netcracker.backend.service;

import edu.netcracker.backend.message.response.CarrierRevenueResponse;
import edu.netcracker.backend.message.response.CarrierViewsResponse;
import edu.netcracker.backend.message.response.ServiceDistributionElement;
import edu.netcracker.backend.message.response.TripDistributionElement;
import edu.netcracker.backend.model.User;

import java.time.LocalDate;
import java.util.List;

public interface StatisticsService {

    List<ServiceDistributionElement> getServiceStatistics();
    List<TripDistributionElement> getTripsStatistics();

    CarrierRevenueResponse getTripsSalesStatistics(long id);

    CarrierRevenueResponse getTripsSalesStatistics(long id, LocalDate from, LocalDate to);

    CarrierRevenueResponse getServicesSalesStatistics(long id);

    CarrierRevenueResponse getServicesSalesStatistics(long id, LocalDate from, LocalDate to);

    List<CarrierRevenueResponse> getTripSalesStatisticsByWeek(long id, LocalDate from, LocalDate to);

    List<CarrierRevenueResponse> getTripSalesStatisticsByMonth(long id, LocalDate from, LocalDate to);

    List<CarrierRevenueResponse> getServicesSalesStatisticsByWeek(long id, LocalDate from, LocalDate to);

    List<CarrierRevenueResponse> getServicesSalesStatisticsByMonth(long id, LocalDate from, LocalDate to);

    List<CarrierViewsResponse> getTripsViewsStatisticsByWeek(long carrierId, LocalDate from, LocalDate to);

    List<CarrierViewsResponse> getTripsViewsStatisticsByMonth(long carrierId, LocalDate from, LocalDate to);

    List<CarrierViewsResponse> getTripsViewsStatisticsByTripByWeek(User caller,
                                                                   long tripId,
                                                                   LocalDate from,
                                                                   LocalDate to);

    List<CarrierViewsResponse> getTripsViewsStatisticsByTripByMonth(User caller,
                                                                    long tripId,
                                                                    LocalDate from,
                                                                    LocalDate to);

    List<CarrierViewsResponse> getServiceViewsStatisticsByWeek(long carrierId, LocalDate from, LocalDate to);

    List<CarrierViewsResponse> getServiceViewsStatisticsByMonth(long carrierId, LocalDate from, LocalDate to);

    List<CarrierViewsResponse> getServiceViewsStatisticsByServiceByWeek(User caller,
                                                                        long serviceId,
                                                                        LocalDate from,
                                                                        LocalDate to);

    List<CarrierViewsResponse> getServiceViewsStatisticsByServiceByMonth(User caller,
                                                                         long serviceId,
                                                                         LocalDate from,
                                                                         LocalDate to);
}
