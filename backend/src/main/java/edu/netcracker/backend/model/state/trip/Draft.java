package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import org.springframework.stereotype.Component;

@Component
public class Draft extends TripState{

    private final static int value = 1;

    @Override
    public boolean isStateChangeAllowed(Trip trip, User user, TripState tripState) {
        return false;
    }

    @Override
    public int getValue() {
        return value;
    }
}
