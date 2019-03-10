package edu.netcracker.backend.model.state.trip;

import edu.netcracker.backend.message.response.TripDTO;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import org.springframework.context.ApplicationContext;

@FunctionalInterface
public interface TripStateAction {

    boolean apply(ApplicationContext ctx, Trip trip, TripDTO tripDTO, User requestUser);
}