package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Service;

import java.util.List;
import java.util.Optional;

public interface ServiceDAO {
    void save(Service service);

    void delete(Service service);

    Optional<Service> find(Number id);


}
