package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.PossibleService;
import edu.netcracker.backend.model.Ticket;
import edu.netcracker.backend.model.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
public class BoughtTicketDTO {

    @JsonProperty("ticket_id")
    private Long ticketId;

    @JsonProperty("passenger_id")
    private Integer passengerId;

    @JsonProperty("p_services_ids")
    private List<Long> pServicesIds;
}
