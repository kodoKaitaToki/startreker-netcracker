package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import org.springframework.stereotype.Component;

@Component
public class Draft implements TripState {

    public final static int DATABASE_VALUE = 1;
    public final static String NAME = "DRAFT";

    @Override
    public boolean isStateChangeAllowed(Trip trip, User user) {
        return false;
    }

    @Override
    public int getDatabaseValue() {
        return DATABASE_VALUE;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
