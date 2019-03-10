package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.message.request.*;
import edu.netcracker.backend.message.response.TripDTO;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.TripWithArrivalAndDepartureData;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.model.state.trip.TripState;
import edu.netcracker.backend.model.state.trip.TripStateRegistry;
import edu.netcracker.backend.service.SuggestionService;
import edu.netcracker.backend.service.TicketClassService;
import edu.netcracker.backend.service.TripService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Trip updateTrip(User requestUser, TripDTO tripDTO) {
        Optional<Trip> optionalTrip = tripDAO.find(tripDTO.getTripId());

        if (!optionalTrip.isPresent()) {
            throw new RequestException("Illegal operation", HttpStatus.NOT_FOUND);
        } else {
            return updateTrip(requestUser, optionalTrip.get(), tripDTO);
        }
    }

    @Override
    public List<TripWithArrivalAndDepartureDataDTO> getAllTripsWithTicketClassAndDiscountsBelongToCarrier(Number carrierId) {
        logger.debug("get all trips with related ticket classes and discounts that belong to carrier with id "
                     + carrierId);

        List<TripWithArrivalAndDepartureData> trips = tripDAO.getAllTripsWitArrivalAndDepatureDataBelongToCarrier(
                carrierId);
        List<DiscountTicketClassDTO> ticketClassDTOs = ticketClassService.getTicketClassesRelatedToCarrier(carrierId);

        return createTripWithArrivalAndDepartureDataAndTicketClassesDTOs(trips, ticketClassDTOs);
    }

    @Override
    public List<TripWithArrivalAndDepartureDataDTO> getAllTripsWithSuggestionAndDiscountsBelongToCarrier(Number carrierId) {
        logger.debug("get all trips with related suggestion and discounts that belong to carrier with id " + carrierId);

        List<TripWithArrivalAndDepartureData> trips = tripDAO.getAllTripsWitArrivalAndDepatureDataBelongToCarrier(
                carrierId);

        Map<Long, List<DiscountSuggestionDTO>> suggestionsRelatedToTrip
                = suggestionService.getSuggestionsRelatedToTicketClasses(ticketClassService.getAllTicketClassesBelongToTrips(
                trips.stream()
                     .map(TripWithArrivalAndDepartureData::getTripId)
                     .collect(Collectors.toList())));
        return createTripWithArrivalAndDepartureDataAndSuggestionDTOs(trips, suggestionsRelatedToTrip);
    }


    private Trip updateTrip(User requestUser, Trip trip, TripDTO tripDTO) {
        TripState dtoState = tripStateRegistry.getState(tripDTO.getStatus());

        if (!dtoState.equals(trip.getTripState())) {
            changeStatus(requestUser, trip, dtoState, tripDTO);
        }

        tripDAO.save(trip);
        return trip;
    }

    private void changeStatus(User requestUser, Trip trip, TripState tripState, TripDTO tripDTO) {
        if (!trip.changeStatus(requestUser, tripState, tripDTO)) {
            throw new RequestException("Illegal operation", HttpStatus.FORBIDDEN);
        }
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