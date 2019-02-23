package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.StatisticsDAO;
import edu.netcracker.backend.message.response.CarrierStatisticsResponse;
import edu.netcracker.backend.message.response.ServiceDistributionElement;
import edu.netcracker.backend.message.response.TripDistributionElement;
import edu.netcracker.backend.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        return statisticsDAO.getTripsSalesStatistics(id, from, to);
    }

    public CarrierStatisticsResponse getServicesSalesStatistics(long id){
        return statisticsDAO.getServiceSalesStatistics(id);
    }

    public CarrierStatisticsResponse getServicesSalesStatistics(long id, LocalDate from, LocalDate to){
        return statisticsDAO.getServiceSalesStatistics(id, from, to);
    }
}
