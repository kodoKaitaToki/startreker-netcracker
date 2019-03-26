package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BoughtTicketDTO {

    @JsonProperty("class_id")
    private Long classId;

    @JsonProperty("bought_ticket_id")
    private Long boughtTicketId;

    @JsonProperty("p_services_ids")
    private List<Long> pServicesIds;
}
