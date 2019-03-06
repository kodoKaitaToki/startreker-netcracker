package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class Published extends TripState {

    private final static int value = 4;

    private static List<Integer> allowedStatesToSwitchFrom =
            Arrays.asList(3, 6);

    @Override
    public boolean isStateChangeAllowed(Trip trip, User requestUser, TripState tripState) {
        return requestUser.equals(trip.getApprover()) && allowedStatesToSwitchFrom.contains(tripState.getValue());
    }

    @Override
    public int getValue() {
        return value;
    }
}
