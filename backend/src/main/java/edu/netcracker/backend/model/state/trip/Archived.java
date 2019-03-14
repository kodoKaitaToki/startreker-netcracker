package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class Archived extends TripState {

    private final String stringValue = "Archived";

    private final static int databaseValue = 5;

    private static List<Integer> allowedStatesToSwitchFrom = Collections.singletonList(4);

    @Override
    public boolean isStateChangeAllowed(Trip trip, User requestUser, TripState tripState) {
        return requestUser.equals(trip.getOwner()) && allowedStatesToSwitchFrom.contains(tripState.getDatabaseValue());
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
