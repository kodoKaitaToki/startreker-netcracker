package edu.netcracker.backend.service;

import edu.netcracker.backend.message.response.ReportStatisticsResponse;
import edu.netcracker.backend.service.impl.ReportStatisticsBuilderImpl;

public interface ReportStatisticsBuilder {
    ReportStatisticsResponse build();
    ReportStatisticsBuilderImpl addTotalCount();
    ReportStatisticsBuilderImpl addTotalOpenedCount();
    ReportStatisticsBuilderImpl addTotalAnsweredCount();
    ReportStatisticsBuilderImpl addTotalRatedCount();
    ReportStatisticsBuilderImpl addTotalReOpenedCount();
    ReportStatisticsBuilderImpl addTotalInProgressCount();
    ReportStatisticsBuilderImpl addTotalFinishedCount();
    ReportStatisticsBuilderImpl forUser(Long id);
    ReportStatisticsBuilderImpl addAverageRate();
}
