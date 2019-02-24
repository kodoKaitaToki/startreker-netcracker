package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.SpaceportDAO;
import edu.netcracker.backend.dao.TicketDAO;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.dao.UserDAO;
import edu.netcracker.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    private TicketDAO ticketDAO;
    private UserDAO userDAO;
    private SpaceportDAO spaceportDAO;
    private TripDAO tripDAO;

    @Autowired
    public AdminServiceImpl(TicketDAO ticketDAO, UserDAO userDAO, SpaceportDAO spaceportDAO, TripDAO tripDAO) {
        this.ticketDAO = ticketDAO;
        this.userDAO = userDAO;
        this.spaceportDAO = spaceportDAO;
        this.tripDAO = tripDAO;
    }

    @Override
    public Map<Integer, Integer> getCostsPerPeriodPerCarrier(Number id, LocalDateTime from, LocalDateTime to) {
        HashMap<Integer, Integer> prices = new HashMap<>();

        tripDAO.findByCarrierId(id).stream()
                .filter(trip -> trip.getDepartureDate().isAfter(from)
                        && trip.getDepartureDate().isBefore(to))
                .flatMap(trip -> trip.getTicketClasses().stream())
                .forEach(ticketClass -> {
                    int count = ticketDAO.findAllByClass(ticketClass.getClassId()).size();
                    prices.merge(ticketClass.getTicketPrice(), count, (a, b) -> a + b);
                });

        return prices;
    }

    @Override
    public Map<Integer, Integer> getCostsPerPeriod(LocalDateTime from, LocalDateTime to) {
        HashMap<Integer, Integer> prices = new HashMap<>();

        tripDAO.findAll().stream()
                .filter(trip -> trip.getDepartureDate().isAfter(from)
                        && trip.getDepartureDate().isBefore(to))
                .flatMap(trip -> trip.getTicketClasses().stream())
                .forEach(ticketClass -> {
                    int count = ticketDAO.findAllByClass(ticketClass.getClassId()).size();
                    prices.merge(ticketClass.getTicketPrice(), count, (a, b) -> a + b);
                });

        return prices;
    }

    @Override
    public Map<LocalDateTime, Integer> getUsersIncreasingPerPeriod(LocalDateTime from, LocalDateTime to) {
        HashMap<LocalDateTime, Integer> inc = new HashMap<>();

        userDAO.findPerPeriod(from, to).forEach(user -> inc.merge(user.getRegistrationDate(), 1, (a, b) -> a + b));

        return inc;
    }

    @Override
    public Map<LocalDateTime, Integer> getCarriersIncreasingPerPeriod(LocalDateTime from, LocalDateTime to) {
        HashMap<LocalDateTime, Integer> inc = new HashMap<>();

        userDAO.findPerPeriodByRole(1, from, to).forEach(user -> inc.merge(user.getRegistrationDate(), 1, (a, b) -> a + b));

        return inc;
    }

    @Override
    public Map<LocalDateTime, Integer> getLocationsIncreasingPerPeriod(LocalDateTime from, LocalDateTime to) {
        HashMap<LocalDateTime, Integer> inc = new HashMap<>();

        spaceportDAO.findPerPeriod(from, to).forEach(spaceport -> inc.merge(spaceport.getCreationDate(), 1, (a, b) -> a + b));

        return inc;
    }

}
