package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.security.SecurityContext;
import edu.netcracker.backend.utils.TripStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TripServiceImpl {

    private final TripDAO tripDAO;
    private final SecurityContext securityContext;

    @Autowired
    public TripServiceImpl(TripDAO tripDAO, SecurityContext securityContext) {
        this.tripDAO = tripDAO;
        this.securityContext = securityContext;
    }

    public Trip open(long id) {
        Trip trip = tripDAO.find(id).orElseThrow(
                () -> new RequestException("Trip " + id + " not found ", HttpStatus.NOT_FOUND));
        isTripOwner(trip);
        isInAllowedState(trip, TripStatus.DRAFT, TripStatus.UNDER_CLARIFICATION, TripStatus.ARCHIVED);
        trip.setTripStatus(TripStatus.OPEN.getValue());
        trip.setApproverId(null);
        tripDAO.save(trip);
        return trip;
    }

    public Trip assign(long id) {
        Trip trip = tripDAO.find(id).orElseThrow(
                () -> new RequestException("Trip " + id + " not found ", HttpStatus.NOT_FOUND));
        isInAllowedState(trip, TripStatus.OPEN);
        trip.setTripStatus(TripStatus.ASSIGNED.getValue());
        trip.setApproverId(securityContext.getUser().getUserId());
        tripDAO.save(trip);
        return trip;
    }

    public Trip archive(long id) {
        Trip trip = tripDAO.find(id).orElseThrow(
                () -> new RequestException("Trip " + id + " not found ", HttpStatus.NOT_FOUND));
        isTripOwner(trip);
        isInAllowedState(trip, TripStatus.PUBLISHED);
        trip.setTripStatus(TripStatus.ARCHIVED.getValue());
        tripDAO.save(trip);
        return trip;
    }

    public Trip publish(long id) {
        Trip trip = tripDAO.find(id).orElseThrow(
                () -> new RequestException("Trip " + id + " not found ", HttpStatus.NOT_FOUND));
        isInAllowedState(trip, TripStatus.ASSIGNED);
        isAssignedApprover(trip);
        trip.setTripStatus(TripStatus.PUBLISHED.getValue());
        tripDAO.save(trip);
        return trip;
    }

    public Trip clarify(long id, String message) {
        Trip trip = tripDAO.find(id).orElseThrow(
                () -> new RequestException("Trip " + id + " not found ", HttpStatus.NOT_FOUND));
        isInAllowedState(trip, TripStatus.ASSIGNED);
        isAssignedApprover(trip);
        trip.setTripStatus(TripStatus.UNDER_CLARIFICATION.getValue());
        // todo process message
        tripDAO.save(trip);
        return trip;
    }

    public void remove(long id) {
        Trip trip = tripDAO.find(id).orElseThrow(
                () -> new RequestException("Trip " + id + " not found", HttpStatus.NOT_FOUND));
        isAssignedApprover(trip);
        isInAllowedState(trip, TripStatus.UNDER_CLARIFICATION, TripStatus.DRAFT, TripStatus.ARCHIVED);
        trip.setTripStatus(TripStatus.REMOVED.getValue());
        tripDAO.save(trip);
    }

    private void isTripOwner(Trip trip) throws RequestException {
        if (!tripDAO.findOwner(trip).equals(securityContext.getUser())) {
            throw new RequestException("You are not an owner of this trip", HttpStatus.FORBIDDEN);
        }
    }

    private void isAssignedApprover(Trip trip) throws RequestException {
        if(!trip.getApproverId().equals(securityContext.getUser().getUserId())){
            throw new RequestException("You are not an assigned approver of this trip", HttpStatus.FORBIDDEN);
        }
    }

    private void isInAllowedState(Trip trip, TripStatus ... statuses){
        for(TripStatus tripStatus : statuses){
            if(trip.getTripStatus().longValue() == tripStatus.getValue()) {
                return;
            }
        }
        throw new RequestException(
                "This action is not allowed in current trip state",
                HttpStatus.BAD_REQUEST
        );
    }
}