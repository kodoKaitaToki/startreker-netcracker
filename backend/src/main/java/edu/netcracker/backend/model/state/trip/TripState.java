package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;

public abstract class TripState {

    public abstract boolean isStateChangeAllowed(Trip trip, User user, TripState tripState);
    public abstract int getValue();
}
