package edu.netcracker.backend.service;

import edu.netcracker.backend.message.response.ReportStatisticsResponse;
import edu.netcracker.backend.service.impl.ReportStatisticsBuilderImpl;

public interface ReportStatisticsBuilder {
    ReportStatisticsResponse build();
    ReportStatisticsBuilder addTotalCount();
    ReportStatisticsBuilder addTotalOpenedCount();
    ReportStatisticsBuilder addTotalAnsweredCount();
    ReportStatisticsBuilder addTotalRatedCount();
    ReportStatisticsBuilder addTotalReOpenedCount();
    ReportStatisticsBuilder addTotalInProgressCount();
    ReportStatisticsBuilder addTotalFinishedCount();
    ReportStatisticsBuilder forUser(Long id);
    ReportStatisticsBuilder addAverageRate();
}
