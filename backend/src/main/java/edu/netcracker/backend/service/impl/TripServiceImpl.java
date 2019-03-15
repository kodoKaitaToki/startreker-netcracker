package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.message.request.*;
import edu.netcracker.backend.message.response.TripDTO;
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
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TripServiceImpl implements TripService {

    private static final Logger logger = LoggerFactory.getLogger(TripServiceImpl.class);

    private static final String DATE_PATTERN = "dd-MM-yyyy HH:mm";

    private final TripDAO tripDAO;
    private final TripStateRegistry tripStateRegistry;

    private final TicketClassService ticketClassService;

    private final SuggestionService suggestionService;

    @Autowired
    public TripServiceImpl(TripDAO tripDAO,
                           TripStateRegistry tripStateRegistry,
                           TicketClassService ticketClassService,
                           SuggestionService suggestionService) {
        this.tripDAO = tripDAO;
        this.tripStateRegistry = tripStateRegistry;
        this.ticketClassService = ticketClassService;
        this.suggestionService = suggestionService;
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
}