package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.TicketClass;
import edu.netcracker.backend.dao.VehicleDAO;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class VehicleDAOImpl extends CrudDAO<Vehicle> implements VehicleDAO {

    private TicketClass ticketClass;
    private final String findAllTrips = "SELECT trip_id FROM trip WHERE vehicle_id = ?";
    private final String findAll = "SELECT * FROM vehicle";
    private final String findByOwnerId = "SELECT * FROM vehicle WHERE owner_id = ?";

    @Override
    public Optional<Vehicle> find(Number id) {
        Optional<Vehicle> vehicleOpt = super.find(id);

        if (vehicleOpt.isPresent()) {
            return attachTrips(vehicleOpt.get());
        }

        return Optional.empty();
    }

    public List<Vehicle> findByOwnerId(Number id) {
        List<Vehicle> vehicles = new ArrayList<>();

        vehicles.addAll(getJdbcTemplate().query(
                findByOwnerId,
                new Object[]{id},
                getGenericMapper()));

        vehicles.forEach(this::attachTrips);

        return vehicles;
    }

    @Override
    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = new ArrayList<>();

        vehicles.addAll(getJdbcTemplate().query(
                findAll,
                getGenericMapper()));

        vehicles.forEach(this::attachTrips);

        return vehicles;
    }

    @Autowired
    public VehicleDAOImpl(TicketClass ticketClass) {
        this.ticketClass = ticketClass;
    }

    private Optional<Vehicle> attachTrips(Vehicle vehicle) {
        List<Long> rows = getJdbcTemplate().queryForList(findAllTrips, Long.class, vehicle.getVehicleId());
        List<Trip> trips = new ArrayList<>();

        for (Long trip_id : rows) {
            trips.add(ticketClass.find(trip_id).orElse(null));
        }

        vehicle.setVehicleTrips(trips);
        return Optional.of(vehicle);
    }

}
