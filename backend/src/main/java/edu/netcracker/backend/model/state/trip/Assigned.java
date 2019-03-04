package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.utils.AuthorityUtils;

import java.util.Collections;
import java.util.List;

public class Assigned extends TripState {

    private final static int value = 3;

    private static List<Integer> allowedStatesToSwitchFrom =
            Collections.singletonList(2);

    @Override
    public boolean isStateChangeAllowed(Trip trip, User requestUser, TripState tripState) {
        if(requestUser.getUserRoles().contains(AuthorityUtils.ROLE_APPROVER)
                && allowedStatesToSwitchFrom.contains(tripState.getValue())){
            trip.setApprover(requestUser);
            return true;
        }
        return false;
    }

    @Override
    public int getValue() {
        return value;
    }
}
