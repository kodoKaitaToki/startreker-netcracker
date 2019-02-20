package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.impl.StatisticsDAO;
import edu.netcracker.backend.message.response.ReportStatisticsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ReportStatisticsBuilder {

    private StatisticsDAO statisticsDAO;
    private ReportStatisticsResponse resp;
    private Long userId;

    @Autowired
    public ReportStatisticsBuilder(StatisticsDAO statisticsDAO) {
        this.statisticsDAO = statisticsDAO;
        this.resp = new ReportStatisticsResponse();
        this.resp.setAmount(new HashMap<>());
    }

    public ReportStatisticsResponse build(){
        ReportStatisticsResponse response = resp;
        resp = new ReportStatisticsResponse();

        Map<String, Double> fullStat =
                userId == null
                ? statisticsDAO.getTroubleTicketStatistics()
                : statisticsDAO.getTroubleTicketStatisticsByApprover(userId);

        for(Map.Entry<String, Double> map : response.getAmount().entrySet()){
            response.getAmount().put(map.getKey(), fullStat.get(map.getKey()));
        }

        return response;
    }

    public ReportStatisticsBuilder addTotalCount(){
        resp.getAmount().put("total", null);
        return this;
    }

    public ReportStatisticsBuilder addTotalOpenedCount(){
        resp.getAmount().put("total_opened", null);
        return this;
    }

    public ReportStatisticsBuilder addTotalAnsweredCount(){
        resp.getAmount().put("total_answered", null);
        return this;
    }

    public ReportStatisticsBuilder addTotalRatedCount(){
        resp.getAmount().put("total_rated", null);
        return this;
    }

    public ReportStatisticsBuilder addTotalReOpenedCount(){
        resp.getAmount().put("total_reopened", null);
        return this;
    }

    public ReportStatisticsBuilder addTotalInProgressCount(){
        resp.getAmount().put("total_in_progress", null);
        return this;
    }

    public ReportStatisticsBuilder addTotalFinishedCount(){
        resp.getAmount().put("total_resolved", null);
        return this;
    }

    public ReportStatisticsBuilder forUser(Long id){
        this.userId = id;
        return this;
    }

    public ReportStatisticsBuilder addAverageRate(){
        resp.getAmount().put("avg_rate", null);
        return this;
    }
}
