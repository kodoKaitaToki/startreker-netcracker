package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleDAO {

    void save(Vehicle vehicle);

    Optional<Vehicle> find(Number id);

    void delete(Vehicle vehicle);

    List<Vehicle> findAll();

    List<Vehicle> findByOwnerId(Number id);

}
