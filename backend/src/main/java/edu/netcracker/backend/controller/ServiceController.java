package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.request.TimeInterval;
import edu.netcracker.backend.message.response.CarrierStatisticsResponse;
import edu.netcracker.backend.message.response.ServiceDistributionElement;
import edu.netcracker.backend.security.SecurityContext;
import edu.netcracker.backend.service.impl.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ServiceController {

    private final StatisticsService statisticsService;
    private final SecurityContext securityContext;

    @Autowired
    public ServiceController(StatisticsService statisticsService, SecurityContext securityContext) {
        this.statisticsService = statisticsService;
        this.securityContext = securityContext;
    }

    @GetMapping("v1/api/service/distribution")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<ServiceDistributionElement> getServiceStatistics(){
        return statisticsService.getServiceStatistics();
    }

    @GetMapping(value = "v1/api/service/sales")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public CarrierStatisticsResponse getServicesSalesStatistics(TimeInterval timeInterval){
        return timeInterval != null && timeInterval.isProvided()
                ? statisticsService.getServicesSalesStatistics(
                securityContext.getUser().getUserId(),
                timeInterval.getFrom(),
                timeInterval.getTo())

                : statisticsService.getServicesSalesStatistics(securityContext.getUser().getUserId());
    }
}
