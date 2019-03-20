package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BoughtTicketDTO {

    @JsonProperty("ticket_id")
    private Long ticketId;

    @JsonProperty("passenger_id")
    private Integer passengerId;

    @JsonProperty("p_services_ids")
    private List<Long> pServicesIds;
}
