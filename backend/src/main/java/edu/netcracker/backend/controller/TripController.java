package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.request.MandatoryTimeInterval;
import edu.netcracker.backend.message.request.OptionalTimeInterval;
import edu.netcracker.backend.message.request.trips.TripCreation;
import edu.netcracker.backend.message.response.CarrierStatisticsResponse;
import edu.netcracker.backend.message.response.TripDistributionElement;
import edu.netcracker.backend.message.response.trips.AllCarriersTripsDTO;
import edu.netcracker.backend.security.SecurityContext;
import edu.netcracker.backend.service.StatisticsService;
import edu.netcracker.backend.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

    @GetMapping("api/v1/trips")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<AllCarriersTripsDTO> getAllTrips() {return tripService.getAllTripsForCarrier();}

    @PostMapping("api/v1/trips")
//    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public void saveTrip(@RequestBody TripCreation trip) {tripService.saveTrip(trip);}

    @GetMapping("api/v1/trip/distribution")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
}
