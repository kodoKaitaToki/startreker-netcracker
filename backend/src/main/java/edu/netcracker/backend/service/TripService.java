package edu.netcracker.backend.service;

import edu.netcracker.backend.message.request.TripWithArrivalAndDepartureDataDTO;
import edu.netcracker.backend.message.request.trips.TripCreation;
import edu.netcracker.backend.message.response.trips.ReadTripsDTO;
import edu.netcracker.backend.message.request.TripRequest;
import edu.netcracker.backend.message.response.ReadTripsDTO;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;

import java.util.List;

public interface TripService {

    Trip updateTrip(User requestUser, TripRequest tripDTO);

    List<TripWithArrivalAndDepartureDataDTO> getAllTripsWithTicketClassAndDiscountsBelongToCarrier(Number carrierId);

    List<TripWithArrivalAndDepartureDataDTO> getAllTripsWithSuggestionAndDiscountsBelongToCarrier(Number carrierId);

    List<Trip> findCarrierTripsByStatus(User requestUser, String status, Long offset, Long limit);

    List<Trip> findCarrierTrips(User requestUser, Long offset, Long limit);

    List<Trip> findApproverTrips(User requestUser, String status, Long offset, Long limit);

    void saveTrip(TripCreation tripCreation);

    List<ReadTripsDTO> getAllTripsForCarrier();

    List<ReadTripsDTO> getAllTripsForCarrier(Long carrierId);

    List<ReadTripsDTO> getAllTripsForCarrierWithPagination(Integer limit, Integer offset);

    List<ReadTripsDTO> getAllTripsForUser(String departurePlanet,
                                          String departureSpaceport,
                                          String departureDate,
                                          String arrivalPlanet,
                                          String arrivalSpaceport,
                                          Integer limit,
                                          Integer offset);

    void updateTripForCarrier(TripCreation trip, Long tripId);
}
