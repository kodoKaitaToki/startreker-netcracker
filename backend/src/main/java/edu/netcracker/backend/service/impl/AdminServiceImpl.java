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

    private StatisticsDAO statisticsDAO;

    @Autowired
    public AdminServiceImpl(StatisticsDAO statisticsDAO) {
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
    public Map<LocalDateTime, Long> getUsersIncreasingPerPeriod(LocalDateTime from, LocalDateTime to) {
        return statisticsDAO.getUsersIncreasingPerPeriod(from, to);
    }

    @Override
    public Map<LocalDateTime, Long> getCarriersIncreasingPerPeriod(LocalDateTime from, LocalDateTime to) {
        return statisticsDAO.getUsersIncreasingByRoleIdPerPeriod(3, from, to);
    }

    @Override
    public Map<LocalDateTime, Long> getLocationsIncreasingPerPeriod(LocalDateTime from, LocalDateTime to) {
        return statisticsDAO.getLocationsIncreasingPerPeriod(from, to);
    }
}
