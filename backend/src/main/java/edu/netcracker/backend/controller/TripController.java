package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.request.MandatoryTimeInterval;
import edu.netcracker.backend.message.request.OptionalTimeInterval;
import edu.netcracker.backend.message.response.CarrierStatisticsResponse;
import edu.netcracker.backend.message.response.TripDTO;
import edu.netcracker.backend.message.response.TripDistributionElement;
import edu.netcracker.backend.security.SecurityContext;
import edu.netcracker.backend.service.StatisticsService;
import edu.netcracker.backend.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
public class TripController {

    private final StatisticsService statisticsService;
    private final SecurityContext securityContext;
    private final TripService tripService;

    @Autowired
    public TripController(StatisticsService statisticsService, SecurityContext securityContext, TripService tripService) {
        this.statisticsService = statisticsService;
        this.securityContext = securityContext;
        this.tripService = tripService;
    }

    @GetMapping("api/v1/trip/distribution")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<TripDistributionElement> getTripsStatistics() {
        return statisticsService.getTripsStatistics();
    }

    @GetMapping(value = "api/v1/trip/sales")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public CarrierStatisticsResponse getTripsSalesStatistics(OptionalTimeInterval timeInterval) {
        return timeInterval != null && timeInterval.isProvided()
                ? statisticsService.getTripsSalesStatistics(
                securityContext.getUser().getUserId(),
                timeInterval.getFrom(),
                timeInterval.getTo())

                : statisticsService.getTripsSalesStatistics(securityContext.getUser().getUserId());
    }

    @GetMapping(value = "api/v1/trip/sales/per_week")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierStatisticsResponse> getTripsSalesStatisticsByWeek(
            @Valid MandatoryTimeInterval timeInterval) {
        return statisticsService.getTripSalesStatisticsByWeek(
                securityContext.getUser().getUserId(),
                timeInterval.getFrom(),
                timeInterval.getTo());
    }

    @GetMapping(value = "api/v1/trip/sales/per_month")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierStatisticsResponse> getTripsSalesStatisticsByMonth(
            @Valid MandatoryTimeInterval timeInterval) {
        return statisticsService.getTripSalesStatisticsByMonth(
                securityContext.getUser().getUserId(),
                timeInterval.getFrom(),
                timeInterval.getTo());
    }

    @PostMapping(value = "api/v1/trip/status/open/{id}")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public TripDTO open(@PathVariable("id") Long id) {
        return TripDTO.from(tripService.open(id));
    }

    @PostMapping(value = "api/v1/trip/status/archive/{id}")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public TripDTO archive(@PathVariable("id") Long id) {
        return TripDTO.from(tripService.archive(id));
    }

    @PostMapping(value = "api/v1/trip/status/remove/{id}")
    @PreAuthorize("hasAuthority('ROLE_APPROVER')")
    public void remove(@PathVariable("id") Long id) {
        tripService.remove(id);
    }

    @PostMapping(value = "api/v1/trip/status/publish/{id}")
    @PreAuthorize("hasAuthority('ROLE_APPROVER')")
    public TripDTO publish(@PathVariable("id") Long id) {
        return TripDTO.from(tripService.publish(id));
    }

    @PostMapping(value = "api/v1/trip/status/assign/{id}")
    @PreAuthorize("hasAuthority('ROLE_APPROVER')")
    public TripDTO assign(@PathVariable("id") Long id) {
        return TripDTO.from(tripService.assign(id));
    }

    @PostMapping(value = "api/v1/trip/status/clarify/{id}", consumes = MediaType.APPLICATION_JSON)
    @PreAuthorize("hasAuthority('ROLE_APPROVER')")
    public TripDTO clarify(@PathVariable("id") Long id, @RequestBody @Max(5000) String message) {
        return TripDTO.from(tripService.clarify(id, message));
    }
}
