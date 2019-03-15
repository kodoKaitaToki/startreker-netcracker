package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.message.request.TripRequest;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.utils.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class Assigned implements TripState {

    public final static int DATABASE_VALUE = 3;
    public final static String NAME = "ASSIGNED";

    private static List<Integer> allowedStatesToSwitchFrom = Collections.singletonList(2);

    @Override
    public boolean isStateChangeAllowed(Trip trip, User requestUser) {
        if (requestUser.getUserRoles()
                       .contains(AuthorityUtils.ROLE_APPROVER) && allowedStatesToSwitchFrom.contains(trip.getTripState()
                                                                                                         .getDatabaseValue())) {
            trip.setApprover(requestUser);
            return true;
        }
        return false;
    }

    @Override
    public boolean switchTo(Trip trip, User requestUser, TripRequest tripRequest) {
        trip.setApprover(requestUser);
        trip.setTripState(this);
        return true;
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
