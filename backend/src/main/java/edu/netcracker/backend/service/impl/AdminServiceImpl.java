package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.SpaceportDAO;
import edu.netcracker.backend.dao.TicketDAO;
import edu.netcracker.backend.dao.UserDAO;
import edu.netcracker.backend.dao.VehicleDAO;
import edu.netcracker.backend.model.Spaceport;
import edu.netcracker.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    private VehicleDAO vehicleDAO;
    private TicketDAO ticketDAO;
    private UserDAO userDAO;
    private SpaceportDAO spaceportDAO;

    @Autowired
    public AdminServiceImpl(VehicleDAO vehicleDAO, TicketDAO ticketDAO, UserDAO userDAO, SpaceportDAO spaceportDAO) {
        this.vehicleDAO = vehicleDAO;
        this.ticketDAO = ticketDAO;
        this.userDAO = userDAO;
        this.spaceportDAO = spaceportDAO;
    }

    @Override
    public Map<Integer, Integer> getCostsPerPeriodPerCarrier(Number id, LocalDate from, LocalDate to) {
        HashMap<Integer, Integer> prices = new HashMap<>();

        vehicleDAO.findByOwnerId(id).stream()
                .flatMap(vehicle -> vehicle.getVehicleTrips().stream())
                .filter(trip -> trip.getDepartureDate().isAfter(from.atStartOfDay())
                        && trip.getDepartureDate().isBefore(to.atTime(LocalTime.MAX)))
                .flatMap(trip -> trip.getTicketClasses().stream())
                .forEach(ticketClass -> {
                    int count = ticketDAO.findAllByClass(ticketClass.getClassId()).size();
                    prices.merge(ticketClass.getTicketPrice(), count, (a, b) -> a + b);
                });

        return prices;
    }

    @Override
    public Map<Integer, Integer> getCostsPerPeriod(LocalDate from, LocalDate to) {
        HashMap<Integer, Integer> prices = new HashMap<>();

        vehicleDAO.findAll().stream()
                .flatMap(vehicle -> vehicle.getVehicleTrips().stream())
                .filter(trip -> trip.getDepartureDate().isAfter(from.atStartOfDay())
                        && trip.getDepartureDate().isBefore(to.atTime(LocalTime.MAX)))
                .flatMap(trip -> trip.getTicketClasses().stream())
                .forEach(ticketClass -> {
                    int count = ticketDAO.findAllByClass(ticketClass.getClassId()).size();
                    prices.merge(ticketClass.getTicketPrice(), count, (a, b) -> a + b);
                });

        return prices;
    }

    @Override
    public Map<LocalDate, Integer> getUsersIncreasingPerPeriod(LocalDate from, LocalDate to) {
        HashMap<LocalDate, Integer> inc = new HashMap<>();

        userDAO.findPerPeriod(from, to).forEach(user -> inc.merge(user.getRegistrationDate(), 1, (a, b) -> a + b));

        return inc;
    }

    @Override
    public Map<LocalDate, Integer> getCarriersIncreasingPerPeriod(LocalDate from, LocalDate to) {
        HashMap<LocalDate, Integer> inc = new HashMap<>();

        userDAO.findPerPeriodByRole(1, from, to).forEach(user -> inc.merge(user.getRegistrationDate(), 1, (a, b) -> a + b));

        return inc;
    }

    @Override
    public Map<LocalDate, Integer> getLocationsIncreasingPerPeriod(LocalDate from, LocalDate to) {
        HashMap<LocalDate, Integer> inc = new HashMap<>();

        spaceportDAO.findPerPeriod(from, to).forEach(spaceport -> inc.merge(spaceport.getCreationDate(), 1, (a, b) -> a + b));

        return inc;
    }

}
