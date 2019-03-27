package edu.netcracker.backend.message.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class CarrierRevenueResponse extends CarrierStatisticsResponse {

    private Long sold;
    private Long revenue;
}
