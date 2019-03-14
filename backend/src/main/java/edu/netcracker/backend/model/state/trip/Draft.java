package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import org.springframework.stereotype.Component;

@Component
public class Draft implements TripState {

    public final static int DATABASE_VALUE = 1;

    @Override
    public boolean isStateChangeAllowed(Trip trip, User user) {
        return false;
    }

    @Override
    public int getDatabaseValue() {
        return DATABASE_VALUE;
    }
}
