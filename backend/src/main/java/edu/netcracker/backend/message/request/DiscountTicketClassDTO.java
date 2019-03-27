package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.TicketClass;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DiscountTicketClassDTO {

    @NotNull
    @JsonProperty("class_id")
    private Long classId;

    @NotNull
    @JsonProperty("trip_id")
    private Long tripId;

    @NotNull
    @JsonProperty("ticket_price")
    private Integer ticketPrice;

    @NotNull
    @JsonProperty("class_name")
    private String className;

    @NotNull
    @JsonProperty("class_seats")
    private Integer classSeats;

    @NotNull
    @Valid
    @JsonProperty("discount")
    private DiscountDTO discountDTO;

    public static DiscountTicketClassDTO toTicketClassDTO(TicketClass ticketClass, DiscountDTO discountDTO) {
        DiscountTicketClassDTO ticketClassDTO = new DiscountTicketClassDTO();
        ticketClassDTO.setClassId(ticketClass.getClassId());
        ticketClassDTO.setTripId(ticketClass.getTripId());
        ticketClassDTO.setTicketPrice(ticketClass.getTicketPrice());
        ticketClassDTO.setClassName(ticketClass.getClassName());
        ticketClassDTO.setClassSeats(ticketClass.getClassSeats());
        ticketClassDTO.setDiscountDTO(discountDTO);

        return ticketClassDTO;
    }
}
