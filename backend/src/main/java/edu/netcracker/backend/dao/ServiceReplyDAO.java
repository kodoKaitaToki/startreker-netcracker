package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Report;
import edu.netcracker.backend.model.ServiceReply;

import java.util.Optional;

public interface ServiceReplyDAO {
    void save(ServiceReply reply);

    Optional<ServiceReply> find(Number id);

    void delete(ServiceReply reply);
}
