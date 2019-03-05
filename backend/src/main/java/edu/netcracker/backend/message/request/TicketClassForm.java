package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TicketClassForm {

    @JsonProperty("ticket_class_id")
    private Long ticketClassId;

    @JsonProperty("item_number")
    private Integer itemNumber;
}
