package edu.netcracker.backend.service;

import edu.netcracker.backend.message.request.TripWithArrivalAndDepartureDataAndSuggestionsDTO;
import edu.netcracker.backend.message.request.TripWithArrivalAndDepartureDataAndTicketClassesDTO;
import edu.netcracker.backend.message.request.TripWithArrivalAndDepartureDataDTO;
import edu.netcracker.backend.message.response.TripDTO;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.TripWithArrivalAndDepartureData;
import edu.netcracker.backend.model.User;

import java.util.List;

public interface TripService {

    Trip updateTrip(User requestUser, TripDTO tripDTO);

    List<TripWithArrivalAndDepartureDataDTO> getAllTripsWithTicketClassAndDiscountsBelongToCarrier(Number carrierId);

    List<TripWithArrivalAndDepartureDataDTO> getAllTripsWithSuggestionAndDiscountsBelongToCarrier(Number carrierId);
}
