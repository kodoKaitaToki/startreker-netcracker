package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.dao.TripReplyDAO;
import edu.netcracker.backend.message.request.TripRequest;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.TripReply;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.utils.AuthorityUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Slf4j(topic = "log")
public enum TripState {

    DRAFT(1, "DRAFT") {
        @Override
        public boolean isStateChangeAllowed(Trip trip, User user) {
            return false;
        }
    },

    OPEN(2, "OPEN") {
        private final List<Integer> allowedStatesToSwitchFrom = Arrays.asList(1, 5, 6);

        @Override
        public boolean isStateChangeAllowed(Trip trip, User requestUser) {
            return requestUser.equals(trip.getOwner()) && allowedStatesToSwitchFrom.contains(trip.getTripState()
                                                                                                 .getDatabaseValue());
        }
    },

    ASSIGNED(3, "ASSIGNED") {
        private final List<Integer> allowedStatesToSwitchFrom = Collections.singletonList(2);

        @Override
        public boolean isStateChangeAllowed(Trip trip, User requestUser) {
            return requestUser.getUserRoles()
                              .contains(AuthorityUtils.ROLE_APPROVER)
                   && allowedStatesToSwitchFrom.contains(trip.getTripState()
                                                             .getDatabaseValue());
        }

        @Override
        public boolean switchTo(ApplicationContext ctx, Trip trip, TripRequest tripDTO, User requestUser) {
            trip.setApprover(requestUser);
            trip.setTripState(this);
            return true;
        }
    },

    PUBLISHED(4, "PUBLISHED") {
        private final List<Integer> allowedStatesToSwitchFrom = Arrays.asList(3, 6);

        @Override
        public boolean isStateChangeAllowed(Trip trip, User requestUser) {
            return requestUser.equals(trip.getApprover()) && allowedStatesToSwitchFrom.contains(trip.getTripState()
                                                                                                    .getDatabaseValue());
        }
    },

    ARCHIVED(5, "ARCHIVED") {
        private final List<Integer> allowedStatesToSwitchFrom = Collections.singletonList(4);

        @Override
        public boolean isStateChangeAllowed(Trip trip, User requestUser) {
            return requestUser.equals(trip.getOwner()) && allowedStatesToSwitchFrom.contains(trip.getTripState()
                                                                                                 .getDatabaseValue());
        }
    },

    UNDER_CLARIFICATION(6, "UNDER_CLARIFICATION") {
        private final List<Integer> allowedStatesToSwitchFrom = Collections.singletonList(3);

        @Override
        public boolean isStateChangeAllowed(Trip trip, User requestUser) {
            return requestUser.equals(trip.getApprover()) && allowedStatesToSwitchFrom.contains(trip.getTripState()
                                                                                                    .getDatabaseValue());
        }

        @Override
        public boolean switchTo(ApplicationContext ctx, Trip trip, TripRequest tripDTO, User requestUser) {
            String replyText = tripDTO.getReplies()
                                      .get(0)
                                      .getReplyText();

            log.info("User [id: {}] creating trip reply [text: {}]", requestUser.getUserId(), replyText);

            TripReplyDAO tripReplyDAO = ctx.getBean(TripReplyDAO.class);

            if (tripDTO.getReplies()
                       .get(0) == null) {
                return false;
            }

            TripReply tripReply = new TripReply();
            tripReply.setCreationDate(LocalDateTime.now());
            tripReply.setReportText(replyText);
            tripReply.setTripId(trip.getTripId());
            tripReply.setWriterId(requestUser.getUserId());
            tripReplyDAO.save(tripReply);
            return true;
        }
    },

    REMOVED(7, "REMOVED") {
        private final List<Integer> allowedStatesToSwitchFrom = Arrays.asList(1, 2, 3, 4, 5, 6);

        @Override
        public boolean isStateChangeAllowed(Trip trip, User requestUser) {
            return requestUser.equals(trip.getOwner()) && allowedStatesToSwitchFrom.contains(trip.getTripState()
                                                                                                 .getDatabaseValue());
        }
    };

    TripState(int databaseValue, String name) {
        this.databaseValue = databaseValue;
        this.name = name;
    }

    private int databaseValue;
    private String name;

    private static final Map<Integer, TripState> idRegistry = new HashMap<>();
    private static final Map<String, TripState> nameRegistry = new HashMap<>();

    static {
        for (TripState tripState : TripState.values()) {
            idRegistry.put(tripState.getDatabaseValue(), tripState);
            nameRegistry.put(tripState.getName(), tripState);
        }
    }

    public static TripState getState(int databaseValue) {
        return idRegistry.get(databaseValue);
    }

    public static TripState getState(String name) {
        return nameRegistry.get(name);
    }

    public boolean switchTo(ApplicationContext ctx, Trip trip, TripRequest tripDTO, User requestUser) {
        trip.setTripState(this);
        return true;
    }

    public abstract boolean isStateChangeAllowed(Trip trip, User user);
}