package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.request.TimeInterval;
import edu.netcracker.backend.message.response.CarrierStatisticsResponse;
import edu.netcracker.backend.message.response.TripDistributionElement;
import edu.netcracker.backend.security.SecurityContext;
import edu.netcracker.backend.service.impl.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TripController {

    private final StatisticsService statisticsService;
    private final SecurityContext securityContext;

    @Autowired
    public TripController(StatisticsService statisticsService, SecurityContext securityContext) {
        this.statisticsService = statisticsService;
        this.securityContext = securityContext;
    }

    @GetMapping("v1/api/trip/distribution")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<TripDistributionElement> getTripsStatistics(){
        return statisticsService.getTripsStatistics();
    }

    @GetMapping(value = "v1/api/trip/sales")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public CarrierStatisticsResponse getTripsSalesStatistics(TimeInterval timeInterval){
        return timeInterval != null && timeInterval.isProvided()
                ? statisticsService.getTripsSalesStatistics(
                        securityContext.getUser().getUserId(),
                        timeInterval.getFrom(),
                        timeInterval.getTo())

                : statisticsService.getTripsSalesStatistics(securityContext.getUser().getUserId());
    }
}
