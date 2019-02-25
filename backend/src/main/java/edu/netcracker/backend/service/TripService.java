package edu.netcracker.backend.service;

import edu.netcracker.backend.model.Trip;

public interface TripService {

    Trip open(long id);
    Trip assign(long id);
    Trip archive(long id);
    Trip publish(long id);
    Trip clarify(long id, String message);
    void remove(long id);
}
