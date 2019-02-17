package edu.netcracker.backend.message.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceDistributionElement {

    private Long service_id;
    private String service_name;
    private Long occurrence_count;
    private Double percentage;
}
