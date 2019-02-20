package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.TicketDAO;
import edu.netcracker.backend.dao.VehicleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminServiceImpl {

    private VehicleDAO vehicleDAO;
    private TicketDAO ticketDAO;

    @Autowired
    public AdminServiceImpl(VehicleDAO vehicleDAO, TicketDAO ticketDAO) {
        this.vehicleDAO = vehicleDAO;
        this.ticketDAO = ticketDAO;
    }

    public Map<Integer, Integer> getCostsPerCarrier(Number id) {
        Map<Integer, Integer> prices = new HashMap<>();

        vehicleDAO.findAll().stream()
                .flatMap(vehicle -> vehicle.getVehicleTrips().stream())
                .flatMap(trip -> trip.getTicketClasses().stream())
                .forEach(ticketClass -> {
                    int count = ticketDAO.findAllByClass(ticketClass.getClassId()).size();
                    prices.merge(ticketClass.getTicketPrice(), count, (a, b) -> a + b);
                });

        return prices;


    }

    public Map<Integer, Integer> getCostsPerWeekPerCarrier(Number id) {
        Map<Integer, Integer> prices = new HashMap<>();

        vehicleDAO.findAll().stream()
                .flatMap(vehicle -> vehicle.getVehicleTrips().stream())
                .filter(trip -> LocalDateTime.now().minusWeeks(1).isBefore(trip.getDepartureDate()))
                .flatMap(trip -> trip.getTicketClasses().stream())
                .forEach(ticketClass -> {
                    int count = ticketDAO.findAllByClass(ticketClass.getClassId()).size();
                    prices.merge(ticketClass.getTicketPrice(), count, (a, b) -> a + b);
                });

        return prices;
    }

    public Map<Integer, Integer> getCostsPerMonthPerCarrier(Number id) {
        Map<Integer, Integer> prices = new HashMap<>();

        vehicleDAO.findAll().stream()
                .flatMap(vehicle -> vehicle.getVehicleTrips().stream())
                .filter(trip -> LocalDateTime.now().minusMonths(1).isBefore(trip.getDepartureDate()))
                .flatMap(trip -> trip.getTicketClasses().stream())
                .forEach(ticketClass -> {
                    int count = ticketDAO.findAllByClass(ticketClass.getClassId()).size();
                    prices.merge(ticketClass.getTicketPrice(), count, (a, b) -> a + b);
                });

        return prices;
    }

    public Map<Integer, Integer> getCostsPerPeriodPerCarrier(Number id, LocalDate from, LocalDate to) {
        HashMap<Integer, Integer> prices = new HashMap<>();

        vehicleDAO.findAll().stream()
                .flatMap(vehicle -> vehicle.getVehicleTrips().stream())
                .filter(trip -> trip.getDepartureDate().isAfter(from.atStartOfDay()) && trip.getDepartureDate().isBefore(to.atStartOfDay()))
                .flatMap(trip -> trip.getTicketClasses().stream())
                .forEach(ticketClass -> {
                    int count = ticketDAO.findAllByClass(ticketClass.getClassId()).size();
                    prices.merge(ticketClass.getTicketPrice(), count, (a, b) -> a + b);
                });

        return prices;

    }

}
