package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.dao.TripReplyDAO;
import edu.netcracker.backend.message.response.TripDTO;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.TripReply;
import edu.netcracker.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
public class UnderClarification extends TripState {

    private final TripReplyDAO tripReplyDAO;

    private final static int databaseValue = 6;

    private static List<Integer> allowedStatesToSwitchFrom = Collections.singletonList(3);

    @Autowired
    public UnderClarification(TripReplyDAO tripReplyDAO) {
        this.tripReplyDAO = tripReplyDAO;
    }

    @Override
    public boolean isStateChangeAllowed(Trip trip, User requestUser, TripState tripState) {
        return requestUser.equals(trip.getApprover())
               && allowedStatesToSwitchFrom.contains(tripState.getDatabaseValue());
    }

    @Override
    public int getDatabaseValue() {
        return databaseValue;
    }

    @Override
    public boolean apply(Trip trip, User requestUser, TripState tripState, TripDTO tripDTO) {
        if (tripDTO.getReply() == null) {
            return false;
        }

        TripReply tripReply = new TripReply();
        tripReply.setCreationDate(LocalDateTime.now());
        tripReply.setReportText(tripDTO.getReply());
        tripReply.setTripId(trip.getTripId());
        tripReply.setWriterId(requestUser.getUserId());
        tripReplyDAO.save(tripReply);
        return true;
    }
}
