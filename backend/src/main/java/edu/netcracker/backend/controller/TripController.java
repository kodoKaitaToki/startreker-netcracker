package edu.netcracker.backend.controller;

import edu.netcracker.backend.dao.StatisticsDAO;
import edu.netcracker.backend.message.response.TripDistributionElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TripController {

    private final StatisticsDAO statisticsDAO;

    @Autowired
    public TripController(StatisticsDAO statisticsDAO) {
        this.statisticsDAO = statisticsDAO;
    }

    @PostMapping("api/trip/distribution")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<TripDistributionElement> getTripsStatistics(){
        return statisticsDAO.getTripsStatistics();
    }

}
