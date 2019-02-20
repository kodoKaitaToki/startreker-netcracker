package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.TicketDAO;
import edu.netcracker.backend.dao.VehicleDAO;
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

    @Autowired
    public AdminServiceImpl(VehicleDAO vehicleDAO, TicketDAO ticketDAO) {
        this.vehicleDAO = vehicleDAO;
        this.ticketDAO = ticketDAO;
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

}
