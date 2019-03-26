package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.response.ReportStatisticsResponse;
import edu.netcracker.backend.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/")
public class TroubleTicketController {

    private final StatisticsService statisticsService;

    @Autowired
    public TroubleTicketController(StatisticsService statisticsService) {this.statisticsService = statisticsService;}

    @GetMapping("trouble/statistics")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ReportStatisticsResponse getStatistics() {
        return statisticsService.getTroubleTicketStatistics();
    }

    @GetMapping("approver/{id}/trouble/statistics")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ReportStatisticsResponse getStatistics(@PathVariable Long id) {
        return statisticsService.getTroubleTicketStatisticsByApprover(id);
    }
}