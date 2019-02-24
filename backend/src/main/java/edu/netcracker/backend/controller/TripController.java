package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.request.TimeInterval;
import edu.netcracker.backend.message.response.CarrierStatisticsResponse;
import edu.netcracker.backend.message.response.TripDistributionElement;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.security.SecurityContext;
import edu.netcracker.backend.service.StatisticsService;
import edu.netcracker.backend.service.impl.TripServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
public class TripController {

    private final StatisticsService statisticsService;
    private final SecurityContext securityContext;
    private final TripServiceImpl tripService;

    @Autowired
    public TripController(StatisticsService statisticsService, SecurityContext securityContext, TripServiceImpl tripService) {
        this.statisticsService = statisticsService;
        this.securityContext = securityContext;
        this.tripService = tripService;
    }

    @GetMapping("api/v1/trip/distribution")
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

    @GetMapping(value = "api/v1/trip/sales/per_week")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierStatisticsResponse> getTripsSalesStatisticsByWeek(TimeInterval timeInterval){
        return statisticsService.getTripSalesStatisticsByWeek(
                securityContext.getUser().getUserId(),
                timeInterval.getFrom(),
                timeInterval.getTo());
    }

    @GetMapping(value = "api/v1/trip/sales/per_month")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierStatisticsResponse> getTripsSalesStatisticsByMonth(TimeInterval timeInterval){
        return statisticsService.getTripSalesStatisticsByMonth(
                securityContext.getUser().getUserId(),
                timeInterval.getFrom(),
                timeInterval.getTo());
    }

    @PostMapping(value = "api/v1/trip/status/open/{id}")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public Trip open(@PathVariable("id") Long id){
        return tripService.open(id);
    }

    @PostMapping(value = "api/v1/trip/status/archive/{id}")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public Trip archive(@PathVariable("id") Long id){
        return tripService.archive(id);
    }

    @PostMapping(value = "api/v1/trip/status/remove/{id}")
    @PreAuthorize("hasAuthority('ROLE_APPROVER')")
    public void remove(@PathVariable("id") Long id){
        tripService.remove(id);
    }

    @PostMapping(value = "api/v1/trip/status/publish/{id}")
    @PreAuthorize("hasAuthority('ROLE_APPROVER')")
    public Trip publish(@PathVariable("id") Long id){
        return tripService.publish(id);
    }

    @PostMapping(value = "api/v1/trip/status/assign/{id}")
    @PreAuthorize("hasAuthority('ROLE_APPROVER')")
    public Trip assign(@PathVariable("id") Long id){
        return tripService.assign(id);
    }

    @PostMapping(value = "api/v1/trip/status/clarify/{id}", consumes = MediaType.APPLICATION_JSON)
    @PreAuthorize("hasAuthority('ROLE_APPROVER')")
    public Trip clarify(@PathVariable("id") Long id, @RequestBody @Max(5000) String message){
        return tripService.clarify(id, message);
    }
}
