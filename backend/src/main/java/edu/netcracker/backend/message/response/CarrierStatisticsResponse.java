package edu.netcracker.backend.message.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@EqualsAndHashCode
public class CarrierStatisticsResponse {

    private Long sold;
    private Long revenue;
    private String from;
    private String to;
}
