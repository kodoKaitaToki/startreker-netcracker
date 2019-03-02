package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.Discount;
import edu.netcracker.backend.model.TicketClass;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TicketClassDTO {

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
    @Valid
    @JsonProperty("discount")
    private DiscountDTO discount;

    public static TicketClassDTO toTicketClassDTO(TicketClass ticketClass, Discount discount) {
        TicketClassDTO ticketClassDTO = new TicketClassDTO();
        ticketClassDTO.setClassId(ticketClass.getClassId());
        ticketClassDTO.setTripId(ticketClass.getTripId());
        ticketClassDTO.setTicketPrice(ticketClass.getTicketPrice());
        ticketClassDTO.setDiscount(DiscountDTO.toDiscountDTO(discount));

        return ticketClassDTO;
    }
}
