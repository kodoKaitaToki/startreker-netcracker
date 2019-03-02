package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
