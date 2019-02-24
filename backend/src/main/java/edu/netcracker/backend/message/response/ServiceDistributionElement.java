package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceDistributionElement {

    @JsonProperty("service_id")
    private Long serviceId;
    @JsonProperty("service_name")
    private String serviceName;
    @JsonProperty("occurrence_count")
    private Long occurrenceCount;
    @JsonProperty("percentage")
    private Double percentage;
}
