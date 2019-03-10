package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class Removed extends TripState {

    private final static int databaseValue = 7;

    private static List<Integer> allowedStatesToSwitchFrom = Collections.singletonList(6);

    @Override
    public boolean isStateChangeAllowed(Trip trip, User requestUser, TripState tripState) {
        return requestUser.equals(trip.getOwner()) && allowedStatesToSwitchFrom.contains(tripState.getDatabaseValue());
    }

    @Override
    public int getDatabaseValue() {
        return databaseValue;
    }
}
