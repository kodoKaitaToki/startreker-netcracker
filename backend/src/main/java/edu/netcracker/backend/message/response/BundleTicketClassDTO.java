package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
public class BundleTicketClassDTO extends TicketClassDTO {
    @JsonProperty("item_number")
    private Integer itemNumber;

    @JsonProperty("services")
    private List<BundleServiceDTO> services;

}
