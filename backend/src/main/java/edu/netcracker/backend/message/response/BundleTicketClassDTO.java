package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BundleTicketClassDTO extends TicketClassDTO {
    @JsonProperty("item_number")
    private Integer itemNumber;

}
