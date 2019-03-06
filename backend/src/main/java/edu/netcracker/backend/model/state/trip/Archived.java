package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class Archived extends TripState {

    private final static int value = 5;

    private static List<Integer> allowedStatesToSwitchFrom = Collections.singletonList(4);

    @Override
    public boolean isStateChangeAllowed(Trip trip, User requestUser, TripState tripState) {
        return requestUser.equals(trip.getOwner()) && allowedStatesToSwitchFrom.contains(tripState.getValue());
    }

    @Override
    public int getValue() {
        return value;
    }
}
