package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TicketClassDTO {

    @JsonProperty("class_id")
    private Long classId;

    @JsonProperty("ticket_price")
    private Integer ticketPrice;
}
