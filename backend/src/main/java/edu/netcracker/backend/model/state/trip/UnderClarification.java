package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;

import java.util.Collections;
import java.util.List;

public class UnderClarification extends TripState {

    private final static int value = 6;

    private static List<Integer> allowedStatesToSwitchFrom =
            Collections.singletonList(3);

    @Override
    public boolean isStateChangeAllowed(Trip trip, User requestUser, TripState tripState) {
        return requestUser.equals(trip.getApprover()) && allowedStatesToSwitchFrom.contains(tripState.getValue());
    }

    @Override
    public int getValue() {
        return value;
    }
}
