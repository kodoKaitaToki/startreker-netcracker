package edu.netcracker.backend.service;

import edu.netcracker.backend.message.request.TripWithArrivalAndDepartureDataDTO;
import edu.netcracker.backend.message.request.trips.TripCreation;
import edu.netcracker.backend.message.response.TripDTO;
import edu.netcracker.backend.message.response.trips.ReadTripsDTO;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;

import java.util.List;

public interface TripService {

    void saveTrip(TripCreation tripCreation);

    List<ReadTripsDTO> getAllTripsForCarrier();

    List<ReadTripsDTO> getAllTripsForCarrier(Long carrierId);

    List<ReadTripsDTO> getAllTripsForUser(String departurePlanet,
                                             String departureSpaceport,
                                             String departureDate,
                                             String arrivalPlanet,
                                             String arrivalSpaceport,
                                             Integer limit,
                                             Integer offset);

    Trip updateTrip(User requestUser, TripDTO tripDTO);

    List<TripWithArrivalAndDepartureDataDTO> getAllTripsWithTicketClassAndDiscountsBelongToCarrier(Number carrierId);

    List<TripWithArrivalAndDepartureDataDTO> getAllTripsWithSuggestionAndDiscountsBelongToCarrier(Number carrierId);
}
