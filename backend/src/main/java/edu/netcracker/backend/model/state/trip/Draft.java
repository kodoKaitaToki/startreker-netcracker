package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import org.springframework.stereotype.Component;

@Component
public class Draft extends TripState {

    private final String stringValue = "Draft";

    private final static int databaseValue = 1;

    @Override
    public boolean isStateChangeAllowed(Trip trip, User user, TripState tripState) {
        return false;
    }

    @Override
    public int getDatabaseValue() {
        return databaseValue;
    }

    @Override
    public String getStringValue() {
        return stringValue;
    }
}
