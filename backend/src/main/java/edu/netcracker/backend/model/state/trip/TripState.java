package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.message.response.TripDTO;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;

public abstract class TripState {

    public abstract boolean isStateChangeAllowed(Trip trip, User user, TripState tripState);

    public abstract int getDatabaseValue();

    public abstract String getStringValue();

    public boolean apply(Trip trip, User requestUser, TripState tripState, TripDTO tripDTO) {
        return true;
    }

}
