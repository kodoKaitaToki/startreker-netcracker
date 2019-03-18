package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.request.MandatoryTimeInterval;
import edu.netcracker.backend.message.request.OptionalTimeInterval;
import edu.netcracker.backend.message.request.Pageable;
import edu.netcracker.backend.message.request.TripRequest;
import edu.netcracker.backend.message.request.trips.TripCreation;
import edu.netcracker.backend.message.response.CarrierRevenueResponse;
import edu.netcracker.backend.message.response.CarrierViewsResponse;
import edu.netcracker.backend.message.response.TripResponse;
import edu.netcracker.backend.message.response.TripDistributionElement;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.message.response.trips.ReadTripsDTO;
import edu.netcracker.backend.security.SecurityContext;
import edu.netcracker.backend.service.StatisticsService;
import edu.netcracker.backend.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@PropertySource("classpath:controller/trip.properties")
@Validated
public class TripController {

    private final StatisticsService statisticsService;
    private final SecurityContext securityContext;
    private final TripService tripService;

    @Value("${MAX_TRIPS_PER_REQUEST}")
    private Long MAX_TRIPS_PER_REQUEST;

    @Autowired
    public TripController(StatisticsService statisticsService,
                          SecurityContext securityContext,
                          TripService tripService) {
        this.statisticsService = statisticsService;
        this.securityContext = securityContext;
        this.tripService = tripService;
    }

    @PatchMapping(value = "api/v1/trip/{id}", consumes = MediaType.APPLICATION_JSON)
    @PreAuthorize("hasAuthority('ROLE_CARRIER') or hasAuthority('ROLE_APPROVER')")
    public TripResponse update(@Valid @RequestBody TripRequest tripRequest, @PathVariable("id") Long id) {
        tripRequest.setTripId(id);
        return TripResponse.from(tripService.updateTrip(securityContext.getUser(), tripRequest));
    }

    @GetMapping("api/v1/trips")
    //    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<ReadTripsDTO> getAllTrips() {return tripService.getAllTripsForCarrier();}

    @GetMapping("api/v1/carrier/trips")
    //    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<ReadTripsDTO> getAllTripsForCarrier(@RequestParam("carrier_id") Long carrierId) {
        return tripService.getAllTripsForCarrier(carrierId);
    }


    @GetMapping("api/v1/trips/paging")
    //    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<ReadTripsDTO> getAllTripsForCarrierWithPagination(@RequestParam("limit") Integer limit,
                                                                @RequestParam("offset") Integer offset) {
        return tripService.getAllTripsForCarrierWithPagination(limit, offset);
    }

    @GetMapping("api/v1/user/trips")
    public List<ReadTripsDTO> getTripsForUser(@RequestParam("departure_planet") String departurePlanet,
                                              @RequestParam("departure_spaceport") String departureSpaceport,
                                              @RequestParam("departure_date") String departureDate,
                                              @RequestParam("arrival_planet") String arrivalPlanet,
                                              @RequestParam("arrival_spaceport") String arrivalSpaceport,
                                              @RequestParam("limit") Integer limit,
                                              @RequestParam("offset") Integer offset) {
        return tripService.getAllTripsForUser(departurePlanet,
                                              departureSpaceport,
                                              departureDate,
                                              arrivalPlanet,
                                              arrivalSpaceport,
                                              limit,
                                              offset);
    }

    @PostMapping("api/v1/trips")
    //    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public void saveTrip(@RequestBody TripCreation trip) { tripService.saveTrip(trip); }

    @PutMapping("api/v1/trips/{tripId}")
    //    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public void updateTrip(@NotNull @PathVariable("tripId") Long tripId,
                           @RequestBody TripCreation trip) { tripService.updateTripForCarrier(trip, tripId); }

    @GetMapping("api/v1/trip/distribution")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<TripDistributionElement> getTripsStatistics() {
        return statisticsService.getTripsStatistics();
    }

    @GetMapping(value = "api/v1/trip/sales")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public CarrierRevenueResponse getTripsSalesStatistics(OptionalTimeInterval timeInterval) {
        return timeInterval != null && timeInterval.isProvided()
                ? statisticsService.getTripsSalesStatistics(securityContext.getUser()
                                                                           .getUserId(),
                                                            timeInterval.getFrom(),
                                                            timeInterval.getTo())
                : statisticsService.getTripsSalesStatistics(securityContext.getUser()
                                                                           .getUserId());
    }

    @GetMapping(value = "api/v1/trip/sales/per_week")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierRevenueResponse> getTripsSalesStatisticsByWeek(@Valid MandatoryTimeInterval timeInterval) {
        return statisticsService.getTripSalesStatisticsByWeek(securityContext.getUser()
                                                                             .getUserId(),
                                                              timeInterval.getFrom(),
                                                              timeInterval.getTo());
    }

    @GetMapping(value = "api/v1/trip/sales/per_month")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierRevenueResponse> getTripsSalesStatisticsByMonth(@Valid MandatoryTimeInterval timeInterval) {
        return statisticsService.getTripSalesStatisticsByMonth(securityContext.getUser()
                                                                              .getUserId(),
                                                               timeInterval.getFrom(),
                                                               timeInterval.getTo());
    }

    @GetMapping(value = "api/v1/trip/views/per_week")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierViewsResponse> getTripsViewsStatisticsByWeek(@Valid MandatoryTimeInterval timeInterval) {
        return statisticsService.getTripsViewsStatisticsByWeek(securityContext.getUser()
                                                                              .getUserId(),
                                                               timeInterval.getFrom(),
                                                               timeInterval.getTo());
    }

    @GetMapping(value = "api/v1/trip/views/per_month")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierViewsResponse> getTripsViewsStatisticsByMonth(@Valid MandatoryTimeInterval timeInterval) {
        return statisticsService.getTripsViewsStatisticsByMonth(securityContext.getUser()
                                                                               .getUserId(),
                                                                timeInterval.getFrom(),
                                                                timeInterval.getTo());
    }

    @GetMapping(value = "api/v1/trip/{id}/views/per_week")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierViewsResponse> getTripsViewsStatisticsByTripByWeek(@Valid MandatoryTimeInterval timeInterval,
                                                                          @PathVariable("id") Long tripId) {
        return statisticsService.getTripsViewsStatisticsByTripByWeek(securityContext.getUser(),
                                                                     tripId,
                                                                     timeInterval.getFrom(),
                                                                     timeInterval.getTo());
    }

    @GetMapping(value = "api/v1/trip/{id}/views/per_month")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierViewsResponse> getTripsViewsStatisticsByTripByMonth(@Valid MandatoryTimeInterval timeInterval,
                                                                           @PathVariable("id") Long tripId) {
        return statisticsService.getTripsViewsStatisticsByTripByMonth(securityContext.getUser(),
                                                                      tripId,
                                                                      timeInterval.getFrom(),
                                                                      timeInterval.getTo());
    }

    @GetMapping(value = "api/v1/carrier/trip", params = {"status"})
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<TripResponse> getCarrierTripsByStatus(@RequestParam("status") String status, @Valid Pageable pageable) {
        ensureLimit(pageable);
        return toTripDTO(tripService.findCarrierTripsByStatus(securityContext.getUser(),
                                                              status,
                                                              pageable.getOffset(),
                                                              pageable.getLimit()));
    }

    @GetMapping(value = "api/v1/approver/trip", params = {"status"})
    @PreAuthorize("hasAuthority('ROLE_APPROVER')")
    public List<TripResponse> getApproverTripsByStatus(@RequestParam("status") String status, @Valid Pageable pageable) {
        ensureLimit(pageable);
        return toTripDTO(tripService.findApproverTrips(securityContext.getUser(),
                                                       status,
                                                       pageable.getOffset(),
                                                       pageable.getLimit()));
    }

    @GetMapping(value = "api/v1/carrier/trip")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<TripResponse> getCarrierTripsByStatus(@Valid Pageable pageable) {
        ensureLimit(pageable);
        return toTripDTO(tripService.findCarrierTrips(securityContext.getUser(),
                                                      pageable.getOffset(),
                                                      pageable.getLimit()));
    }

    private void ensureLimit(Pageable pageable) {
        if (pageable.getLimit() == null || pageable.getLimit() > MAX_TRIPS_PER_REQUEST) {
            pageable.setLimit(MAX_TRIPS_PER_REQUEST);
        }
    }

    private List<TripResponse> toTripDTO(List<Trip> trips) {
        return trips.stream()
                    .map(TripResponse::from)
                    .collect(Collectors.toList());
    }
}