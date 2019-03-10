package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.dao.TripReplyDAO;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.TripReply;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.utils.AuthorityUtils;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
public enum TripState {

    DRAFT(1) {
        @Override
        public boolean isStateChangeAllowed(Trip trip, User user) {
            return false;
        }
    },

    OPEN(2) {
        private final List<Integer> allowedStatesToSwitchFrom = Arrays.asList(1, 5, 6);

        @Override
        public boolean isStateChangeAllowed(Trip trip, User requestUser) {
            return requestUser.equals(trip.getOwner()) && allowedStatesToSwitchFrom.contains(trip.getTripState()
                                                                                                 .getDatabaseValue());
        }
    },

    ASSIGNED(3) {
        private final List<Integer> allowedStatesToSwitchFrom = Collections.singletonList(2);

        @Override
        public boolean isStateChangeAllowed(Trip trip, User requestUser) {
            if (requestUser.getUserRoles()
                           .contains(AuthorityUtils.ROLE_APPROVER)
                && allowedStatesToSwitchFrom.contains(trip.getTripState()
                                                          .getDatabaseValue())) {
                return true;
            }
            return false;
        }

        @Override
        public TripStateAction getAction() {
            return (ctx, trip, tripDTO, requestUser) -> {
                trip.setApprover(requestUser);
                return true;
            };
        }
    },

    PUBLISHED(4) {
        private final List<Integer> allowedStatesToSwitchFrom = Arrays.asList(3, 6);

        @Override
        public boolean isStateChangeAllowed(Trip trip, User requestUser) {
            return requestUser.equals(trip.getApprover()) && allowedStatesToSwitchFrom.contains(trip.getTripState()
                                                                                                    .getDatabaseValue());
        }
    },

    ARCHIVED(5) {
        private final List<Integer> allowedStatesToSwitchFrom = Collections.singletonList(4);

        @Override
        public boolean isStateChangeAllowed(Trip trip, User requestUser) {
            return requestUser.equals(trip.getOwner()) && allowedStatesToSwitchFrom.contains(trip.getTripState()
                                                                                                 .getDatabaseValue());
        }
    },

    UNDER_CLARIFICATION(6) {
        private final List<Integer> allowedStatesToSwitchFrom = Collections.singletonList(3);

        @Override
        public boolean isStateChangeAllowed(Trip trip, User requestUser) {
            return requestUser.equals(trip.getApprover()) && allowedStatesToSwitchFrom.contains(trip.getTripState()
                                                                                                    .getDatabaseValue());
        }

        @Override
        public TripStateAction getAction() {
            return (ctx, trip, tripDTO, requestUser) -> {
                TripReplyDAO tripReplyDAO = ctx.getBean(TripReplyDAO.class);

                if (tripDTO.getReplies()
                           .get(0) == null) {
                    return false;
                }

                TripReply tripReply = new TripReply();
                tripReply.setCreationDate(LocalDateTime.now());
                tripReply.setReportText(tripDTO.getReplies()
                                               .get(0)
                                               .getReplyText());
                tripReply.setTripId(trip.getTripId());
                tripReply.setWriterId(requestUser.getUserId());
                tripReplyDAO.save(tripReply);
                return true;
            };
        }
    },

    REMOVED(7) {
        private final List<Integer> allowedStatesToSwitchFrom = Collections.singletonList(6);

        @Override
        public boolean isStateChangeAllowed(Trip trip, User requestUser) {
            return requestUser.equals(trip.getOwner()) && allowedStatesToSwitchFrom.contains(trip.getTripState()
                                                                                                 .getDatabaseValue());
        }
    };

    TripState(int databaseValue) {
        this.databaseValue = databaseValue;
    }

    private static final Map<Integer, TripState> registry = new HashMap<>();

    static {
        for (TripState tripState : TripState.values()) {
            registry.put(tripState.getDatabaseValue(), tripState);
        }
    }

    public static TripState getState(int databaseValue) {
        TripState tripState = registry.get(databaseValue);
        if (tripState == null) {
            throw new IllegalArgumentException();
        }
        return tripState;
    }

    private int databaseValue;

    public TripStateAction getAction() {
        return (ctx, trip, tripDTO, requestUser) -> true;
    }

    public abstract boolean isStateChangeAllowed(Trip trip, User user);
}