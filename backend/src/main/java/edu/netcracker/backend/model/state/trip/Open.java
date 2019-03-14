package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Open extends TripState {

    private final String stringValue = "Opened";

    private final static int databaseValue = 2;

    private static List<Integer> allowedStatesToSwitchFrom = Arrays.asList(1, 5, 6);

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