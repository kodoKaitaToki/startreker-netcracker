package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ReportStatisticsResponse {

    @JsonProperty("total")
    private double total;
    @JsonProperty("total_resolved")
    private double totalResolved;
    @JsonProperty("total_answered")
    private double totalAnswered;
    @JsonProperty("total_in_progress")
    private double totalInProgress;
    @JsonProperty("total_open")
    private double totalOpen;
    @JsonProperty("total_reopened")
    private double totalReOpened;
    @JsonProperty("total_rated")
    private double totalRated;
    @JsonProperty("total_removed")
    private double totalRemoved;
    @JsonProperty("average")
    private double averageMark;
}