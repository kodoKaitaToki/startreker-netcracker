package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.PlanetDAO;
import edu.netcracker.backend.dao.SpaceportDAO;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.message.request.*;
import edu.netcracker.backend.message.request.trips.TripCreation;
import edu.netcracker.backend.message.response.trips.ReadTripsDTO;
import edu.netcracker.backend.model.*;
import edu.netcracker.backend.model.state.trip.TripState;
import edu.netcracker.backend.security.SecurityContext;
import edu.netcracker.backend.service.SuggestionService;
import edu.netcracker.backend.service.TicketClassService;
import edu.netcracker.backend.service.TripService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TripServiceImpl implements TripService {

    private TripDAO tripDAO;

    private PlanetDAO planetDAO;

    private SpaceportDAO spaceportDAO;

    private ApplicationContext applicationContext;

    private final TicketClassService ticketClassService;

    private final SuggestionService suggestionService;
    private final SecurityContext securityContext;

    private static final String DATE_PATTERN = "dd-MM-yyyy HH:mm";

    @Autowired
    public TripServiceImpl(TripDAO tripDAO,
                           PlanetDAO planetDAO,
                           SpaceportDAO spaceportDAO,
                           ApplicationContext applicationContext,
                           TicketClassService ticketClassService,
                           SuggestionService suggestionService,
                           SecurityContext securityContext) {
        this.tripDAO = tripDAO;
        this.planetDAO = planetDAO;
        this.spaceportDAO = spaceportDAO;
        this.applicationContext = applicationContext;
        this.ticketClassService = ticketClassService;
        this.suggestionService = suggestionService;
        this.securityContext = securityContext;
    }

    @Override
    public List<ReadTripsDTO> getAllTripsForCarrier() {
        log.debug("Getting all trips for carrier from TripDAO");
        Long carrierId = Long.valueOf(securityContext.getUser()
                                                     .getUserId());
        List<Trip> trips = tripDAO.allCarriersTrips(carrierId);

        if (trips.size() == 0) {
            log.error("No trips were found for carrier with id {}", carrierId);
            throw new RequestException("No trips found", HttpStatus.NOT_FOUND);
        }

        return getAllTripsDTO(trips);
    }

    @Override
    public List<ReadTripsDTO> getAllTripsForCarrier(Long carrierId) {
        log.debug("Getting all trips for carrier from TripDAO");
        List<Trip> trips = tripDAO.allCarriersTrips(carrierId);

        if (trips.size() == 0) {
            log.error("No trips found for carrier with id {}", carrierId);
            throw new RequestException("No trips found", HttpStatus.NOT_FOUND);
        }

        return getAllTripsDTO(trips);
    }

    @Override
    public List<ReadTripsDTO> getAllTripsForCarrierWithPagination(Integer limit, Integer offset) {
        log.debug("Getting {} trips for carrier from TripDAO with pagination starting from {}", limit, offset);
        Long carrierId = Long.valueOf(securityContext.getUser()
                                                     .getUserId());
        List<Trip> trips = tripDAO.paginationForCarrier(limit, offset, carrierId);

        if (trips.size() == 0) {
            log.error("No trips were found for carrier with id {} starting from {}", carrierId, offset);
            throw new RequestException("No trips found", HttpStatus.NOT_FOUND);
        }

        return getAllTripsDTO(trips);
    }

    @Override
    public List<ReadTripsDTO> getAllTripsForUser(String departurePlanet,
                                                 String departureSpaceport,
                                                 String departureDate,
                                                 String arrivalPlanet,
                                                 String arrivalSpaceport,
                                                 Integer limit,
                                                 Integer offset) {
        log.debug("Getting all trips for user from TripDAO");
        List<Trip> trips = tripDAO.getAllTripsForUser(departurePlanet,
                                                      departureSpaceport,
                                                      departureDate,
                                                      arrivalPlanet,
                                                      arrivalSpaceport,
                                                      limit,
                                                      offset);

        log.debug("Remove ticket classes where all tickets are sold");
        for (Trip trip : trips) {
            trip.getTicketClasses()
                .removeIf(ticketClass -> ticketClass.getRemainingSeats() == 0);
        }

        log.debug("Remove trip where all tickets are sold");
        trips.removeIf(trip -> trip.getTicketClasses()
                                   .isEmpty());

        if (trips.size() == 0) {
            log.error("No trips were found with specified criteria");
            throw new RequestException("No trips found", HttpStatus.NOT_FOUND);
        }

        return getAllTripsDTO(trips);
    }

    @Override
    public void saveTrip(TripCreation tripCreation) {
        log.debug("Saving trip from request DTO");
        Trip trip = parseTripFromTripCreation(tripCreation);
        tripDAO.add(trip);
    }

    @Override
    public void updateTripForCarrier(TripCreation tripToUpdate, Long tripId) {
        Trip trip = parseTripFromTripCreation(tripToUpdate);
        trip.setTripId(tripId);
        log.debug("Updating trip info");
        tripDAO.updateTripInfo(trip);
    }

    @Override
    public Trip updateTrip(User requestUser, TripRequest tripRequest) {
        Optional<Trip> optionalTrip = tripDAO.find(tripRequest.getTripId());

        if (!optionalTrip.isPresent()) {
            log.warn("Carrier [id: {}] trying to patch non-existing trip [id: {}]",
                     requestUser.getUserId(),
                     tripRequest.getTripId());
            throw new RequestException("Illegal operation", HttpStatus.NOT_FOUND);
        } else {
            return updateTrip(requestUser, optionalTrip.get(), tripRequest);
        }
    }

    @Override
    public List<Trip> findCarrierTripsByStatus(User requestUser, String status, Long offset, Long limit) {
        TripState state = TripState.getState(status);
        if (state == TripState.REMOVED) {
            return new ArrayList<>();
        }

        log.info("Carrier [id: {}] trying to find own's trips by status [{}], paginate offset [{}], limit [{}]",
                 requestUser.getUserId(),
                 status,
                 offset,
                 limit);
        return tripDAO.findAllByCarrierAndStatus(requestUser.getUserId(), state.getDatabaseValue(), offset, limit);
    }

    @Override
    public List<Trip> findCarrierTrips(User requestUser, Long offset, Long limit) {
        log.info("Carrier [id: {}] trying to find own's trips, paginate offset [{}], limit [{}]",
                 requestUser.getUserId(),
                 offset,
                 limit);
        return tripDAO.findAllByCarrier(requestUser.getUserId(), TripState.REMOVED.getDatabaseValue(), offset, limit);
    }

    @Override
    public List<TripWithArrivalAndDepartureDataDTO> getAllTripsWithTicketClassAndDiscountsBelongToCarrier(Number carrierId) {
        log.debug("get all trips with related ticket classes and discounts that belong to carrier with id "
                  + carrierId);

        List<TripWithArrivalAndDepartureData> trips =
                tripDAO.getAllTripsWitArrivalAndDepatureDataBelongToCarrier(carrierId);
        if (trips.isEmpty()) {
            log.warn("No active trips for carrier {}", carrierId);
            return new ArrayList<>();
        }
        List<DiscountTicketClassDTO> ticketClassDTOs = ticketClassService.getTicketClassesRelatedToCarrier(carrierId);

        return createTripWithArrivalAndDepartureDataAndTicketClassesDTOs(trips, ticketClassDTOs);
    }

    @Override
    public List<TripWithArrivalAndDepartureDataDTO> getAllTripsWithSuggestionAndDiscountsBelongToCarrier(Number carrierId) {
        log.debug("get all trips with related suggestion and discounts that belong to carrier with id " + carrierId);

        List<TripWithArrivalAndDepartureData> trips =
                tripDAO.getAllTripsWitArrivalAndDepatureDataBelongToCarrier(carrierId);

        if (trips.isEmpty()) {
            log.warn("No active trips for carrier {}", carrierId);
            return new ArrayList<>();
        }

        Map<Long, List<DiscountSuggestionDTO>> suggestionsRelatedToTrip =
                suggestionService.getSuggestionsRelatedToTicketClasses(ticketClassService.getAllTicketClassesBelongToTrips(
                        trips.stream()
                             .map(TripWithArrivalAndDepartureData::getTripId)
                             .collect(Collectors.toList())));

        return createTripWithArrivalAndDepartureDataAndSuggestionDTOs(trips, suggestionsRelatedToTrip);
    }


    @Override
    public List<Trip> findApproverTrips(User requestUser, String status, Long offset, Long limit) {
        TripState state = TripState.getState(status);
        log.info("Approver [id: {}] trying to find trips in status [{}], paginate offset [{}], limit [{}]",
                 requestUser.getUserId(),
                 status,
                 offset,
                 limit);
        if (state == TripState.OPEN) {
            return tripDAO.findAllByStatus(state.getDatabaseValue(), offset, limit);
        }
        if (state == TripState.ASSIGNED) {
            return tripDAO.findAllByApproverByStatus(requestUser.getUserId(), state.getDatabaseValue(), offset, limit);
        }

        log.warn("Approver [id: {}] trying to find trips in illegal status [{}], paginate offset [{}], limit [{}]",
                 requestUser.getUserId(),
                 status,
                 offset,
                 limit);

        throw new RequestException("Illegal operation", HttpStatus.FORBIDDEN);
    }

    private Trip updateTrip(User requestUser, Trip trip, TripRequest tripRequest) {
        TripState desiredState = TripState.getState(tripRequest.getStatus());

        if (!desiredState.equals(trip.getTripState())) {
            log.info("Carrier [id: {}] trying to switch trip [id: {}] state from {} to {}",
                     requestUser.getUserId(),
                     trip.getTripId(),
                     trip.getTripState()
                         .getName(),
                     tripRequest.getStatus());
            startStatusChange(requestUser, trip, desiredState, tripRequest);
        }

        tripDAO.save(trip);
        return tripDAO.find(trip.getTripId())
                      .orElseThrow(RequestException::new);
    }

    private void startStatusChange(User requestUser, Trip trip, TripState tripState, TripRequest tripRequest) {
        if (!changeStatus(requestUser, tripState, tripRequest, trip)) {
            log.warn("Carrier [id: {}] trying illegally to switch trip [id: {}] state from {} to {}",
                     Objects.requireNonNull(requestUser)
                            .getUserId(),
                     trip.getTripId(),
                     trip.getTripState()
                         .getName(),
                     tripRequest.getStatus());
            throw new RequestException("Illegal operation", HttpStatus.FORBIDDEN);
        }
    }

    private boolean changeStatus(User requestUser, TripState newTripState, TripRequest tripRequest, Trip trip) {
        if (requestUser == null
            || newTripState == null
            || trip.getTripState() == null
            || !newTripState.isStateChangeAllowed(trip, requestUser)) {

            return false;
        }

        log.info("Carrier [id: {}] switching trip [id: {}] state from {} to {}",
                 Objects.requireNonNull(requestUser)
                        .getUserId(),
                 trip.getTripId(),
                 trip.getTripState()
                     .getName(),
                 tripRequest.getStatus());
        return newTripState.switchTo(applicationContext, trip, tripRequest, requestUser);
    }

    private List<TripWithArrivalAndDepartureDataDTO> createTripWithArrivalAndDepartureDataAndSuggestionDTOs(List<TripWithArrivalAndDepartureData> trips,
                                                                                                            Map<Long, List<DiscountSuggestionDTO>> suggestionsRelatedToTrips) {
        if (suggestionsRelatedToTrips.isEmpty()) {
            log.warn("No suggestions for trips {}", trips);
            return new ArrayList<>();
        }

        List<TripWithArrivalAndDepartureDataDTO> tripDTOs = new ArrayList<>();

        for (TripWithArrivalAndDepartureData trip : trips) {
            List<DiscountSuggestionDTO> suggestionsRelatedToTrip = suggestionsRelatedToTrips.get(trip.getTripId());

            if (suggestionsRelatedToTrip.isEmpty()) {
                continue;
            }

            tripDTOs.add(createTripWithArrivalAndDepartureDataAndSuggestionDTO(trip, suggestionsRelatedToTrip));
        }

        return tripDTOs;
    }

    private TripWithArrivalAndDepartureDataDTO createTripWithArrivalAndDepartureDataAndSuggestionDTO(
            TripWithArrivalAndDepartureData trip,
            List<DiscountSuggestionDTO> suggestionsRelatedToTrip) {

        return TripWithArrivalAndDepartureDataAndSuggestionsDTO.form(trip, suggestionsRelatedToTrip, DATE_PATTERN);
    }

    private List<TripWithArrivalAndDepartureDataDTO> createTripWithArrivalAndDepartureDataAndTicketClassesDTOs(List<TripWithArrivalAndDepartureData> trips,
                                                                                                               List<DiscountTicketClassDTO> ticketClassDTOs) {
        List<TripWithArrivalAndDepartureDataDTO> tripDTOs = new ArrayList<>();
        for (TripWithArrivalAndDepartureData trip : trips) {
            tripDTOs.add(createTripWithArrivalAndDepartureDataAndTicketClassesDTO(trip, ticketClassDTOs));
        }

        return tripDTOs;
    }

    private TripWithArrivalAndDepartureDataDTO createTripWithArrivalAndDepartureDataAndTicketClassesDTO(
            TripWithArrivalAndDepartureData trip,
            List<DiscountTicketClassDTO> ticketClassDTOs) {

        List<DiscountTicketClassDTO> relatedTicketClassDTOs = new ArrayList<>();
        for (DiscountTicketClassDTO ticketClassDTO : ticketClassDTOs) {
            if (trip.getTripId()
                    .equals(ticketClassDTO.getTripId())) {
                relatedTicketClassDTOs.add(ticketClassDTO);
            }
        }

        return TripWithArrivalAndDepartureDataAndTicketClassesDTO.form(trip, relatedTicketClassDTOs, DATE_PATTERN);
    }

    private Trip parseTripFromTripCreation(TripCreation tripCreation) {
        log.debug("Converting DTO to Trip");
        Trip trip = new Trip();
        trip.setDepartureDate(tripCreation.getDepartureDateTime());
        trip.setArrivalDate(tripCreation.getArrivalDateTime());
        trip.setOwner(securityContext.getUser());
        trip.setTripState(TripState.DRAFT);
        trip.setCreationDate(LocalDateTime.now());
        trip.setTripPhoto("defaultPhoto.png");

        Planet departurePlanet = new Planet(planetDAO.getIdByPlanetName(tripCreation.getDeparturePlanet()),
                                            tripCreation.getDeparturePlanet());
        Planet arrivalPlanet = new Planet(planetDAO.getIdByPlanetName(tripCreation.getArrivalPlanet()),
                                          tripCreation.getArrivalPlanet());

        Spaceport departureSpaceport = new Spaceport();
        departureSpaceport.setSpaceportId(spaceportDAO.getIdBySpaceportName(tripCreation.getDepartureSpaceport(),
                                                                            departurePlanet.getPlanetId()));
        departureSpaceport.setSpaceportName(tripCreation.getDepartureSpaceport());
        departureSpaceport.setPlanetId(departurePlanet.getPlanetId());
        departureSpaceport.setPlanet(departurePlanet);

        Spaceport arrivalSpaceport = new Spaceport();
        arrivalSpaceport.setSpaceportId(spaceportDAO.getIdBySpaceportName(tripCreation.getArrivalSpaceport(),
                                                                          arrivalPlanet.getPlanetId()));
        arrivalSpaceport.setSpaceportName(tripCreation.getArrivalSpaceport());
        arrivalSpaceport.setPlanetId(arrivalPlanet.getPlanetId());
        arrivalSpaceport.setPlanet(arrivalPlanet);

        trip.setDepartureSpaceport(departureSpaceport);
        trip.setArrivalSpaceport(arrivalSpaceport);

        return trip;
    }

    private List<ReadTripsDTO> getAllTripsDTO(List<Trip> trips) {
        log.debug("Converting trips to DTO");
        List<ReadTripsDTO> tripsDTO = new ArrayList<>();
        for (Trip trip : trips) {
            tripsDTO.add(ReadTripsDTO.from(trip));
        }

        return tripsDTO;
    }
}