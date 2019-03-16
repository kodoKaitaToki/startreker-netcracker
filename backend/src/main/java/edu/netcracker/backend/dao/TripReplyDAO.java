package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.TripReply;

public interface TripReplyDAO extends CrudDAO<TripReply>{

    void save(TripReply reply);

    void delete(TripReply reply);
}