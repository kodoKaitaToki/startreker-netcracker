package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.message.request.TripRequest;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;

public interface TripState {

    boolean isStateChangeAllowed(Trip trip, User user);

    int getDatabaseValue();

    default boolean switchTo(Trip trip, User requestUser, TripRequest tripRequest) {
        trip.setTripState(this);
        return true;
    }
}
