package edu.netcracker.backend.message.response;

import lombok.Data;

import java.util.Map;

@Data
public class ReportStatisticsResponse {

    private Map<String, Double> amount;
}