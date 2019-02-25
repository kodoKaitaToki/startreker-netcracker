package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Trip;

import java.util.Optional;

public interface TicketClass {
    void save(Trip trip);

    Optional<Trip> find(Number id);

    void delete(Trip trip);
}
