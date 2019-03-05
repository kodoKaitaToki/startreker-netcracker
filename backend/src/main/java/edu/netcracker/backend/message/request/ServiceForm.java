package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ServiceForm {
    @JsonProperty("service_id")
    private Long serviceId;

    @JsonProperty("item_number")
    private Integer itemNumber;
}
