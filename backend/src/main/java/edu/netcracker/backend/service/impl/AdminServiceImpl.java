package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.*;
import edu.netcracker.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    private TicketDAO ticketDAO;
    private UserDAO userDAO;
    private SpaceportDAO spaceportDAO;
    private TripDAO tripDAO;
    private StatisticsDAO statisticsDAO;

    @Autowired
    public AdminServiceImpl(TicketDAO ticketDAO,
                            UserDAO userDAO,
                            SpaceportDAO spaceportDAO,
                            TripDAO tripDAO,
                            StatisticsDAO statisticsDAO) {
        this.ticketDAO = ticketDAO;
        this.userDAO = userDAO;
        this.spaceportDAO = spaceportDAO;
        this.tripDAO = tripDAO;
        this.statisticsDAO = statisticsDAO;
    }

    @Override
    public Map<Float, Long> getCostsPerPeriodPerCarrier(Number id, LocalDateTime from, LocalDateTime to) {
        return statisticsDAO.getCostsByCarrier(id, from, to);
    }

    @Override
    public Map<Float, Long> getCostsPerPeriod(LocalDateTime from, LocalDateTime to) {
        return statisticsDAO.getCosts(from, to);
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

        userDAO.findPerPeriodByRole(3, from, to).forEach(user -> inc.merge(user.getRegistrationDate(), 1, (a, b) -> a + b));

        return inc;
    }

    @Override
    public Map<LocalDateTime, Integer> getLocationsIncreasingPerPeriod(LocalDateTime from, LocalDateTime to) {
        HashMap<LocalDateTime, Integer> inc = new HashMap<>();

        spaceportDAO.findPerPeriod(from, to).forEach(spaceport -> inc.merge(spaceport.getCreationDate(), 1, (a, b) -> a + b));

        return inc;
    }
}
