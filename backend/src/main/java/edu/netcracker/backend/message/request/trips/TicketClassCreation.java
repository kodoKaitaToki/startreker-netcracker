package edu.netcracker.backend.message.request.trips;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.TicketClass;
import lombok.Getter;

@Getter
public class TicketClassCreation {

    @JsonProperty("class_name")
    private String className;

    @JsonProperty("trip_id")
    private Long tripId;

    @JsonProperty("class_seats")
    private Integer classSeats;

    @JsonProperty("ticket_price")
    private Integer ticketPrice;

    public TicketClass toTicketClass() {
        TicketClass ticketClass = new TicketClass();
        ticketClass.setClassName(this.className);
        ticketClass.setTripId(this.tripId);
        ticketClass.setClassSeats(this.classSeats);
        ticketClass.setTicketPrice(this.ticketPrice);

        return ticketClass;
    }
}
