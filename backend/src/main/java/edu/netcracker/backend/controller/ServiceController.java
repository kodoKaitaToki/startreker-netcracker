package edu.netcracker.backend.controller;

import edu.netcracker.backend.dao.impl.StatisticsDAOImpl;
import edu.netcracker.backend.message.response.ServiceDistributionElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ServiceController {

    private final StatisticsDAOImpl statisticsDAO;

    @Autowired
    public ServiceController(StatisticsDAOImpl statisticsDAO) {
        this.statisticsDAO = statisticsDAO;
    }

    @PostMapping("api/service/distribution")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<ServiceDistributionElement> getRouteStatistics(){
        return statisticsDAO.getServicesDistribution();
    }

}
