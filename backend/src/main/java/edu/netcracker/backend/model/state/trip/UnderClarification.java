package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.dao.TripReplyDAO;
import edu.netcracker.backend.message.request.TripRequest;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.TripReply;
import edu.netcracker.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
public class UnderClarification implements TripState {

    public final static int DATABASE_VALUE = 6;
    public final static String NAME = "UNDER_CLARIFICATION";

    private final TripReplyDAO tripReplyDAO;

    private static List<Integer> allowedStatesToSwitchFrom = Collections.singletonList(3);

    @Autowired
    public UnderClarification(TripReplyDAO tripReplyDAO) {
        this.tripReplyDAO = tripReplyDAO;
    }

    @Override
    public boolean isStateChangeAllowed(Trip trip, User requestUser) {
        return requestUser.equals(trip.getApprover()) && allowedStatesToSwitchFrom.contains(trip.getTripState()
                                                                                                .getDatabaseValue());
    }

    @Override
    public int getDatabaseValue() {
        return DATABASE_VALUE;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean switchTo(Trip trip, User requestUser, TripRequest tripRequest) {
        if (tripRequest.getReplies()
                       .get(0) == null) {
            return false;
        }

        TripReply tripReply = new TripReply();
        tripReply.setCreationDate(LocalDateTime.now());
        tripReply.setReportText(tripRequest.getReplies()
                                           .get(0)
                                           .getReplyText());
        tripReply.setTripId(trip.getTripId());
        tripReply.setWriterId(requestUser.getUserId());
        tripReplyDAO.save(tripReply);
        trip.setTripState(this);
        return true;
    }
}
