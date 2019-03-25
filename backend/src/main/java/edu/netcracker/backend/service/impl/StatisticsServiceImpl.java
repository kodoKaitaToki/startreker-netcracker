package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.dao.StatisticsDAO;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.message.response.CarrierRevenueResponse;
import edu.netcracker.backend.message.response.CarrierViewsResponse;
import edu.netcracker.backend.message.response.ServiceDistributionElement;
import edu.netcracker.backend.message.response.TripDistributionElement;
import edu.netcracker.backend.model.ServiceDescr;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j(topic = "log")
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
        log.info("Getting services distribution statistics");
        return statisticsDAO.getServicesDistribution();
    }

    public List<TripDistributionElement> getTripsStatistics() {
        log.info("Getting trips distribution statistics");
        return statisticsDAO.getTripsStatistics();
    }

    public CarrierRevenueResponse getTripsSalesStatistics(long id) {
        log.info("User [id: {}] getting trips sales statistics", id);
        return statisticsDAO.getTripsSalesStatistics(id);
    }

    public CarrierRevenueResponse getTripsSalesStatistics(long id, LocalDate from, LocalDate to) {
        log.info("User [id: {}] getting trips sales statistics from {} to {}", id, from, to);

        CarrierRevenueResponse resp = statisticsDAO.getTripsSalesStatistics(id, from, to);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        resp.setFrom(from.format(formatter));
        resp.setTo(to.format(formatter));
        return resp;
    }

    public CarrierRevenueResponse getServicesSalesStatistics(long id) {
        log.info("User [id: {}] getting services sales statistics", id);
        return statisticsDAO.getServiceSalesStatistics(id);
    }

    public CarrierRevenueResponse getServicesSalesStatistics(long id, LocalDate from, LocalDate to) {
        log.info("User [id: {}] getting services sales statistics from {} to {}", id, from, to);

        CarrierRevenueResponse resp = statisticsDAO.getServiceSalesStatistics(id, from, to);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        resp.setFrom(from.format(formatter));
        resp.setTo(to.format(formatter));
        return resp;
    }

    public List<CarrierRevenueResponse> getTripSalesStatisticsByWeek(long id, LocalDate from, LocalDate to) {
        log.info("User [id: {}] getting trip sales by week from {} to {}", id, from, to);
        return statisticsDAO.getTripsSalesStatisticsByWeek(id, from, to);
    }

    public List<CarrierRevenueResponse> getTripSalesStatisticsByMonth(long id, LocalDate from, LocalDate to) {
        log.info("User [id: {}] getting trip sales by month from {} to {}", id, from, to);
        return statisticsDAO.getTripsSalesStatisticsByMonth(id, from, to);
    }

    public List<CarrierRevenueResponse> getServicesSalesStatisticsByWeek(long id, LocalDate from, LocalDate to) {
        log.info("User [id: {}] getting services sales by week from {} to {}", id, from, to);
        return statisticsDAO.getServicesSalesStatisticsByWeek(id, from, to);
    }

    public List<CarrierRevenueResponse> getServicesSalesStatisticsByMonth(long id, LocalDate from, LocalDate to) {
        log.info("User [id: {}] getting services sales by month from {} to {}", id, from, to);
        return statisticsDAO.getServicesSalesStatisticsByMonth(id, from, to);
    }

    public List<CarrierViewsResponse> getTripsViewsStatisticsByWeek(long id, LocalDate from, LocalDate to) {
        log.info("User [id: {}] getting trip [id: {}] views by week from {} to {}", id, from, to);
        return statisticsDAO.getTripsViewsStatisticsByWeek(id, from, to);
    }

    public List<CarrierViewsResponse> getTripsViewsStatisticsByMonth(long id, LocalDate from, LocalDate to) {
        log.info("User [id: {}] getting trips views by month from {} to {}", id, from, to);
        return statisticsDAO.getTripsViewsStatisticsByMonth(id, from, to);
    }

    public List<CarrierViewsResponse> getTripsViewsStatisticsByTripByWeek(User caller,
                                                                          long tripId,
                                                                          LocalDate from,
                                                                          LocalDate to) {
        ensureCallerIsTripOwner(caller, tripId);
        log.info("User [id: {}] getting trip [id: {}] views by trip and by week from {} to {}",
                 caller.getUserId(),
                 tripId,
                 from,
                 to);
        return statisticsDAO.getTripsViewsStatisticsByTripByWeek(tripId, from, to);
    }

    public List<CarrierViewsResponse> getTripsViewsStatisticsByTripByMonth(User caller,
                                                                           long tripId,
                                                                           LocalDate from,
                                                                           LocalDate to) {
        ensureCallerIsTripOwner(caller, tripId);
        log.info("User [id: {}] getting trip [id: {}] views by trip and by month from {} to {}",
                 caller.getUserId(),
                 tripId,
                 from,
                 to);
        return statisticsDAO.getTripsViewsStatisticsByTripByMonth(tripId, from, to);
    }

    public List<CarrierViewsResponse> getServiceViewsStatisticsByWeek(long id, LocalDate from, LocalDate to) {
        log.info("User [id: {}] getting all services views by week from {} to {}", id, from, to);
        return statisticsDAO.getServiceViewsStatisticsByWeek(id, from, to);
    }

    public List<CarrierViewsResponse> getServiceViewsStatisticsByMonth(long id, LocalDate from, LocalDate to) {
        log.info("User [id: {}] getting all services views by month from {} to {}", id, from, to);
        return statisticsDAO.getServiceViewsStatisticsByMonth(id, from, to);
    }

    public List<CarrierViewsResponse> getServiceViewsStatisticsByServiceByWeek(User caller,
                                                                               long serviceId,
                                                                               LocalDate from,
                                                                               LocalDate to) {
        ensureCallerIsServiceOwner(caller, serviceId);
        log.info("User [id: {}] getting service [id: {}] views by service and by week from {} to {}",
                 caller.getUserId(),
                 serviceId,
                 from,
                 to);
        return statisticsDAO.getServiceViewsStatisticsByServiceByWeek(serviceId, from, to);
    }

    public List<CarrierViewsResponse> getServiceViewsStatisticsByServiceByMonth(User caller,
                                                                                long serviceId,
                                                                                LocalDate from,
                                                                                LocalDate to) {
        ensureCallerIsServiceOwner(caller, serviceId);
        log.info("User [id: {}] getting service [id: {}] views by service and by month from {} to {}",
                 caller.getUserId(),
                 serviceId,
                 from,
                 to);
        return statisticsDAO.getServiceViewsStatisticsByServiceByMonth(serviceId, from, to);
    }

    private void ensureCallerIsTripOwner(User caller, long tripId) {
        Trip trip = tripDAO.find(tripId)
                           .orElseThrow(() -> new RequestException("Trip " + tripId + " not found ",
                                                                   HttpStatus.NOT_FOUND));
        if (!trip.getOwner()
                 .equals(caller)) {
            log.warn("User [id: {}] trying to access someone else's (or non-existing) trip [id: {}]",
                     caller.getUserId(),
                     trip);
            throw new RequestException("Illegal operation", HttpStatus.FORBIDDEN);
        }
    }

    private void ensureCallerIsServiceOwner(User caller, long serviceId) {
        ServiceDescr service = serviceDAO.find(serviceId)
                                         .orElseThrow(() -> new RequestException("Service " + serviceId + " not found ",
                                                                                 HttpStatus.NOT_FOUND));
        if (!service.getCarrierId()
                    .equals(caller.getUserId())) {
            log.warn("User [id: {}] trying to access someone else's (or non-existing) service [id: {}]",
                     caller.getUserId(),
                     serviceId);
            throw new RequestException("Illegal operation", HttpStatus.FORBIDDEN);
        }
    }
}