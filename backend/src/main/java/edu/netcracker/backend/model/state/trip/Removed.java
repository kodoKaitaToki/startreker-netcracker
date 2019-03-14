package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class Removed implements TripState {

    public final static int DATABASE_VALUE = 7;

    private final List<Integer> allowedStatesToSwitchFrom = Collections.singletonList(6);

    @Override
    public boolean isStateChangeAllowed(Trip trip, User requestUser) {
        return requestUser.equals(trip.getOwner()) && allowedStatesToSwitchFrom.contains(trip.getTripState()
                                                                                             .getDatabaseValue());
    }

    @Override
    public int getDatabaseValue() {
        return DATABASE_VALUE;
    }
}
