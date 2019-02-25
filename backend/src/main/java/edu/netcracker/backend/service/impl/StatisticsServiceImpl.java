package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.StatisticsDAO;
import edu.netcracker.backend.message.response.CarrierStatisticsResponse;
import edu.netcracker.backend.message.response.ServiceDistributionElement;
import edu.netcracker.backend.message.response.TripDistributionElement;
import edu.netcracker.backend.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsDAO statisticsDAO;

    @Autowired
    public StatisticsServiceImpl(StatisticsDAO statisticsDAO) {
        this.statisticsDAO = statisticsDAO;
    }

    public List<ServiceDistributionElement> getServiceStatistics() {
        return statisticsDAO.getServicesDistribution();
    }

    public List<TripDistributionElement> getTripsStatistics(){
        return statisticsDAO.getTripsStatistics();
    }

    public CarrierStatisticsResponse getTripsSalesStatistics(long id){
        return statisticsDAO.getTripsSalesStatistics(id);
    }

    public CarrierStatisticsResponse getTripsSalesStatistics(long id, LocalDate from, LocalDate to){
        CarrierStatisticsResponse resp = statisticsDAO.getTripsSalesStatistics(id, from, to);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        resp.setFrom(from.format(formatter));
        resp.setTo(to.format(formatter));
        return resp;
    }

    public CarrierStatisticsResponse getServicesSalesStatistics(long id){
        return statisticsDAO.getServiceSalesStatistics(id);
    }

    public CarrierStatisticsResponse getServicesSalesStatistics(long id, LocalDate from, LocalDate to){
        CarrierStatisticsResponse resp = statisticsDAO.getServiceSalesStatistics(id, from, to);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        resp.setFrom(from.format(formatter));
        resp.setTo(to.format(formatter));
        return resp;
    }

    public List<CarrierStatisticsResponse> getTripSalesStatisticsByWeek(long id, LocalDate from, LocalDate to){
        return statisticsDAO.getTripsSalesStatisticsByWeek(id, from, to);
    }

    public List<CarrierStatisticsResponse> getTripSalesStatisticsByMonth(long id, LocalDate from, LocalDate to){
        return statisticsDAO.getTripsSalesStatisticsByMonth(id, from, to);
    }

    public List<CarrierStatisticsResponse> getServicesSalesStatisticsByWeek(long id, LocalDate from, LocalDate to){
        return statisticsDAO.getServicesSalesStatisticsByWeek(id, from, to);
    }

    public List<CarrierStatisticsResponse> getServicesSalesStatisticsByMonth(long id, LocalDate from, LocalDate to){
        return statisticsDAO.getServicesSalesStatisticsByMonth(id, from, to);
    }
}
