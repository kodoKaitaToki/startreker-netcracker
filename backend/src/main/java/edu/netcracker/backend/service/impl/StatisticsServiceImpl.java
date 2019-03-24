package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.dao.StatisticsDAO;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.message.response.*;
import edu.netcracker.backend.model.ServiceDescr;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsDAO statisticsDAO;
    private final TripDAO tripDAO;
    private final ServiceDAO serviceDAO;

    @Autowired
    public StatisticsServiceImpl(StatisticsDAO statisticsDAO, TripDAO tripDAO, ServiceDAO serviceDAO) {
        this.statisticsDAO = statisticsDAO;
        this.tripDAO = tripDAO;
        this.serviceDAO = serviceDAO;
    }

    public List<ServiceDistributionElement> getServiceStatistics() {
        return statisticsDAO.getServicesDistribution();
    }

    public List<TripDistributionElement> getTripsStatistics() {
        return statisticsDAO.getTripsStatistics();
    }

    public CarrierRevenueResponse getTripsSalesStatistics(long id) {
        return statisticsDAO.getTripsSalesStatistics(id);
    }

    public CarrierRevenueResponse getTripsSalesStatistics(long id, LocalDate from, LocalDate to) {
        CarrierRevenueResponse resp = statisticsDAO.getTripsSalesStatistics(id, from, to);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        resp.setFrom(from.format(formatter));
        resp.setTo(to.format(formatter));
        return resp;
    }

    public CarrierRevenueResponse getServicesSalesStatistics(long id) {
        return statisticsDAO.getServiceSalesStatistics(id);
    }

    public CarrierRevenueResponse getServicesSalesStatistics(long id, LocalDate from, LocalDate to) {
        CarrierRevenueResponse resp = statisticsDAO.getServiceSalesStatistics(id, from, to);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        resp.setFrom(from.format(formatter));
        resp.setTo(to.format(formatter));
        return resp;
    }

    public List<CarrierRevenueResponse> getTripSalesStatisticsByWeek(long id, LocalDate from, LocalDate to) {
        return statisticsDAO.getTripsSalesStatisticsByWeek(id, from, to);
    }

    public List<CarrierRevenueResponse> getTripSalesStatisticsByMonth(long id, LocalDate from, LocalDate to) {
        return statisticsDAO.getTripsSalesStatisticsByMonth(id, from, to);
    }

    public List<CarrierRevenueResponse> getServicesSalesStatisticsByWeek(long id, LocalDate from, LocalDate to) {
        return statisticsDAO.getServicesSalesStatisticsByWeek(id, from, to);
    }

    public List<CarrierRevenueResponse> getServicesSalesStatisticsByMonth(long id, LocalDate from, LocalDate to) {
        return statisticsDAO.getServicesSalesStatisticsByMonth(id, from, to);
    }

    public List<CarrierViewsResponse> getTripsViewsStatisticsByWeek(long id, LocalDate from, LocalDate to) {
        return statisticsDAO.getTripsViewsStatisticsByWeek(id, from, to);
    }

    public List<CarrierViewsResponse> getTripsViewsStatisticsByMonth(long id, LocalDate from, LocalDate to) {
        return statisticsDAO.getTripsViewsStatisticsByMonth(id, from, to);
    }

    public List<CarrierViewsResponse> getTripsViewsStatisticsByTripByWeek(User caller,
                                                                          long tripId,
                                                                          LocalDate from,
                                                                          LocalDate to) {
        ensureCallerIsTripOwner(caller, tripId);
        return statisticsDAO.getTripsViewsStatisticsByTripByWeek(tripId, from, to);
    }

    public List<CarrierViewsResponse> getTripsViewsStatisticsByTripByMonth(User caller,
                                                                           long tripId,
                                                                           LocalDate from,
                                                                           LocalDate to) {
        ensureCallerIsTripOwner(caller, tripId);
        return statisticsDAO.getTripsViewsStatisticsByTripByMonth(tripId, from, to);
    }

    public List<CarrierViewsResponse> getServiceViewsStatisticsByWeek(long id, LocalDate from, LocalDate to) {
        return statisticsDAO.getServiceViewsStatisticsByWeek(id, from, to);
    }

    public List<CarrierViewsResponse> getServiceViewsStatisticsByMonth(long id, LocalDate from, LocalDate to) {
        return statisticsDAO.getServiceViewsStatisticsByMonth(id, from, to);
    }

    public List<CarrierViewsResponse> getServiceViewsStatisticsByServiceByWeek(User caller,
                                                                               long serviceId,
                                                                               LocalDate from,
                                                                               LocalDate to) {
        ensureCallerIsServiceOwner(caller, serviceId);
        return statisticsDAO.getServiceViewsStatisticsByServiceByWeek(serviceId, from, to);
    }

    public List<CarrierViewsResponse> getServiceViewsStatisticsByServiceByMonth(User caller,
                                                                                long serviceId,
                                                                                LocalDate from,
                                                                                LocalDate to) {
        ensureCallerIsServiceOwner(caller, serviceId);
        return statisticsDAO.getServiceViewsStatisticsByServiceByMonth(serviceId, from, to);
    }

    @Override
    public ReportStatisticsResponse getTroubleTicketStatistics() {
        return statisticsDAO.getTroubleTicketStatistics();
    }

    @Override
    public ReportStatisticsResponse getTroubleTicketStatisticsByApprover(Long approverId) {
        return statisticsDAO.getTroubleTicketStatisticsByApprover(approverId);
    }

    private void ensureCallerIsTripOwner(User caller, long tripId) {
        Trip trip = tripDAO.find(tripId)
                           .orElseThrow(() -> new RequestException("Trip " + tripId + " not found ",
                                                                   HttpStatus.NOT_FOUND));
        if (!trip.getOwner()
                 .equals(caller)) {
            throw new RequestException("Illegal operation", HttpStatus.FORBIDDEN);
        }
    }

    private void ensureCallerIsServiceOwner(User caller, long serviceId) {
        ServiceDescr service = serviceDAO.find(serviceId)
                                         .orElseThrow(() -> new RequestException("Service " + serviceId + " not found ",
                                                                                 HttpStatus.NOT_FOUND));
        if (!service.getCarrierId()
                    .equals(caller.getUserId())) {
            throw new RequestException("Illegal operation", HttpStatus.FORBIDDEN);
        }
    }
}