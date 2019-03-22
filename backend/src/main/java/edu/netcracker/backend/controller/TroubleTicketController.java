package edu.netcracker.backend.controller;

import edu.netcracker.backend.dao.StatisticsDAO;
import edu.netcracker.backend.message.response.ReportStatisticsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TroubleTicketController {

    private final StatisticsDAO statisticsDAO;

    @Autowired
    public TroubleTicketController(StatisticsDAO statisticsDAO) {this.statisticsDAO = statisticsDAO;}

    @GetMapping("api/v1/trouble/statistics")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ReportStatisticsResponse getStatistics() {
        return statisticsDAO.getTroubleTicketStatistics();
    }

    @GetMapping("api/v1/trouble/statistics/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ReportStatisticsResponse getStatistics(@PathVariable Long id) {
        return statisticsDAO.getTroubleTicketStatisticsByApprover(id);
    }
}
