package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.PlanetDAO;
import edu.netcracker.backend.dao.SpaceportDAO;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.message.request.*;
import edu.netcracker.backend.message.request.trips.TripCreation;
import edu.netcracker.backend.message.response.trips.ReadTripsDTO;
import edu.netcracker.backend.model.*;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.TripWithArrivalAndDepartureData;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.model.state.trip.*;
import edu.netcracker.backend.service.SuggestionService;
import edu.netcracker.backend.service.TicketClassService;
import edu.netcracker.backend.service.TripService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TripServiceImpl implements TripService {

    private TripDAO tripDAO;

    private PlanetDAO planetDAO;

    private SpaceportDAO spaceportDAO;

    private TripStateRegistry tripStateRegistry;

    private final TicketClassService ticketClassService;

    private final SuggestionService suggestionService;

    private static final String DATE_PATTERN = "dd-MM-yyyy HH:mm";

    private static final Logger logger = LoggerFactory.getLogger(TripServiceImpl.class);

    @Autowired
    private Draft draft;


    @Autowired
    public TripServiceImpl(TripDAO tripDAO,
                           PlanetDAO planetDAO,
                           SpaceportDAO spaceportDAO,
                           TripStateRegistry tripStateRegistry,
                           TicketClassService ticketClassService,
                           SuggestionService suggestionService) {
        this.tripDAO = tripDAO;
        this.planetDAO = planetDAO;
        this.spaceportDAO = spaceportDAO;
        this.tripStateRegistry = tripStateRegistry;
        this.ticketClassService = ticketClassService;
        this.suggestionService = suggestionService;
    }

    @Override
    public List<ReadTripsDTO> getAllTripsForCarrier() {
        logger.debug("Getting all trips for carrier from TripDAO");
        //        TODO: implement getting carrier id from access token
        List<Trip> trips = tripDAO.allCarriersTrips(7L);

        return getAllTripsDTO(trips);
    }

    @Override
    public List<ReadTripsDTO> getAllTripsForCarrier(Long carrierId) {
        logger.debug("Getting all trips for carrier from TripDAO");
        List<Trip> trips = tripDAO.allCarriersTrips(carrierId);

        return getAllTripsDTO(trips);
    }

    @Override
    public List<ReadTripsDTO> getAllTripsForCarrierWithPagination(Integer limit, Integer offset) {
        logger.debug("Getting {} trips for carrier from TripDAO with pagination starting from {}", limit, offset);
        //        TODO: implement getting carrier id from access token
        List<Trip> trips = tripDAO.paginationForCarrier(limit, offset, 7L);

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
        logger.debug("Getting all trips for user from TripDAO");
        List<Trip> trips = tripDAO.getAllTripsForUser(departurePlanet,
                                                      departureSpaceport,
                                                      departureDate,
                                                      arrivalPlanet,
                                                      arrivalSpaceport,
                                                      limit,
                                                      offset);

        logger.debug("Remove ticket classes where all tickets are sold");
        for (Trip trip : trips) {
            trip.getTicketClasses()
                .removeIf(ticketClass -> ticketClass.getRemainingSeats() == 0);
        }

        logger.debug("Remove trip where all tickets are sold");
        trips.removeIf(trip -> trip.getTicketClasses()
                                   .isEmpty());

        return getAllTripsDTO(trips);
    }

    @Override
    public void saveTrip(TripCreation tripCreation) {
        logger.debug("Saving trip from request DTO");
        Trip trip = parseTripFromTripCreation(tripCreation);
        tripDAO.add(trip);
    }

    @Override
    public void updateTripForCarrier(TripCreation tripToUpdate, Long tripId) {
        Trip trip = parseTripFromTripCreation(tripToUpdate);
        trip.setTripId(tripId);
        logger.debug("Updating trip info");
        tripDAO.updateTripInfo(trip);
    }

    @Override
    public Trip updateTrip(User requestUser, TripRequest tripRequest) {
        Optional<Trip> optionalTrip = tripDAO.find(tripRequest.getTripId());

        if (!optionalTrip.isPresent()) {
            throw new RequestException("Illegal operation", HttpStatus.NOT_FOUND);
        } else {
            return updateTrip(requestUser, optionalTrip.get(), tripRequest);
        }
    }

    @Override
    public List<Trip> findCarrierTripsByStatus(User requestUser, String status, Long offset, Long limit) {
        TripState state = tripStateRegistry.getState(status);
        if (state.getDatabaseValue() == Removed.DATABASE_VALUE) {
            return new ArrayList<>();
        }

        return tripDAO.findAllByCarrierAndStatus(requestUser.getUserId(), state.getDatabaseValue(), offset, limit);
    }

    @Override
    public List<Trip> findCarrierTrips(User requestUser, Long offset, Long limit) {
        return tripDAO.findAllByCarrier(requestUser.getUserId(), Removed.DATABASE_VALUE, offset, limit);
    }

    @Override
    public List<TripWithArrivalAndDepartureDataDTO> getAllTripsWithTicketClassAndDiscountsBelongToCarrier(Number carrierId) {
        logger.debug("get all trips with related ticket classes and discounts that belong to carrier with id "
                     + carrierId);

        List<TripWithArrivalAndDepartureData> trips =
                tripDAO.getAllTripsWitArrivalAndDepatureDataBelongToCarrier(carrierId);
        List<DiscountTicketClassDTO> ticketClassDTOs = ticketClassService.getTicketClassesRelatedToCarrier(carrierId);

        return createTripWithArrivalAndDepartureDataAndTicketClassesDTOs(trips, ticketClassDTOs);
    }

    @Override
    public List<TripWithArrivalAndDepartureDataDTO> getAllTripsWithSuggestionAndDiscountsBelongToCarrier(Number carrierId) {
        logger.debug("get all trips with related suggestion and discounts that belong to carrier with id " + carrierId);

        List<TripWithArrivalAndDepartureData> trips =
                tripDAO.getAllTripsWitArrivalAndDepatureDataBelongToCarrier(carrierId);

        Map<Long, List<DiscountSuggestionDTO>> suggestionsRelatedToTrip =
                suggestionService.getSuggestionsRelatedToTicketClasses(ticketClassService.getAllTicketClassesBelongToTrips(
                        trips.stream()
                             .map(TripWithArrivalAndDepartureData::getTripId)
                             .collect(Collectors.toList())));
        return createTripWithArrivalAndDepartureDataAndSuggestionDTOs(trips, suggestionsRelatedToTrip);
    }


    @Override
    public List<Trip> findApproverTrips(User requestUser, String status, Long offset, Long limit) {
        TripState state = tripStateRegistry.getState(status);
        if (state.getDatabaseValue() == Open.DATABASE_VALUE) {
            return tripDAO.findAllByStatus(state.getDatabaseValue(), offset, limit);
        }
        if (state.getDatabaseValue() == Assigned.DATABASE_VALUE) {
            return tripDAO.findAllByApproverByStatus(requestUser.getUserId(), state.getDatabaseValue(), offset, limit);
        }
        throw new RequestException("Illegal operation", HttpStatus.FORBIDDEN);
    }

    private Trip updateTrip(User requestUser, Trip trip, TripRequest tripRequest) {
        TripState desiredState = tripStateRegistry.getState(tripRequest.getStatus());

        if (!desiredState.equals(trip.getTripState())) {
            startStatusChange(requestUser, trip, desiredState, tripRequest);
        }

        tripDAO.save(trip);
        return tripDAO.find(trip.getTripId())
                      .orElseThrow(RequestException::new);
    }

    private void startStatusChange(User requestUser, Trip trip, TripState tripState, TripRequest tripRequest) {
        if (!changeStatus(requestUser, tripState, tripRequest, trip)) {
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

        return newTripState.switchTo(trip, requestUser, tripRequest);
    }

    private List<TripWithArrivalAndDepartureDataDTO> createTripWithArrivalAndDepartureDataAndSuggestionDTOs(List<TripWithArrivalAndDepartureData> trips,
                                                                                                            Map<Long, List<DiscountSuggestionDTO>> suggestionsRelatedToTrips) {
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
        logger.debug("Converting DTO to Trip");
        Trip trip = new Trip();
        trip.setDepartureDate(tripCreation.getDepartureDateTime());
        trip.setArrivalDate(tripCreation.getArrivalDateTime());
        //        TODO: implement getting carrier id from access token
        trip.setOwner(new User());
        trip.getOwner()
            .setUserId(7);
        trip.setTripState(draft);
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
        logger.debug("Converting trips to DTO");
        List<ReadTripsDTO> tripsDTO = new ArrayList<>();
        for (Trip trip : trips) {
            tripsDTO.add(ReadTripsDTO.from(trip));
        }

        return tripsDTO;
    }
}