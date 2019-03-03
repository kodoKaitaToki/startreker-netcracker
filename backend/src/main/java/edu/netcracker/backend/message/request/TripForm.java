package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TripForm {

    @JsonProperty("trip_id")
    private Long tripId;

    @JsonProperty("ticket_classes")
    private List<Long> ticketClasses = new ArrayList<>();

}
