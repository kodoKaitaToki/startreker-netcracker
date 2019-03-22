package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportStatisticsResponse {

    @JsonProperty("total")
    private double total;
    @JsonProperty("total_resolved")
    private double totalResolved;
    @JsonProperty("total_answered")
    private double totalAnswered;
    @JsonProperty("total_in_progress")
    private double totalInProgress;
    @JsonProperty("total_opened")
    private double totalOpen;
    @JsonProperty("total_reopened")
    private double totalReOpened;
    @JsonProperty("total_rated")
    private double totalRated;
    @JsonProperty("total_removed")
    private double totalRemoved;
    @JsonProperty("avg_rate")
    private double averageMark;
}