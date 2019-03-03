package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BundleTripDTO {

    @JsonProperty("trip_id")
    private Long tripId;

    @JsonProperty("ticket_classes")
    private List<TicketClassDTO> ticketClasses;
}
