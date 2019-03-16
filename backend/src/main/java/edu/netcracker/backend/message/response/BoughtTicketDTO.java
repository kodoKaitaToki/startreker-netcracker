package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.PossibleService;
import edu.netcracker.backend.model.Ticket;
import edu.netcracker.backend.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoughtTicketDTO {

    @JsonProperty("ticket_id")
    private Integer ticketId;

    @JsonProperty("passenger_id")
    private Integer passengerId;

    private List<Long> pServicesId;

    private BoughtTicketDTO(Integer ticketId, Integer passengerId, List<Long> pServicesId) {
        this.ticketId = ticketId;
        this.passengerId = passengerId;
        this.pServicesId = pServicesId;
    }
    
    public static BoughtTicketDTO from(Ticket ticket, User user, List<Long> pServicesId) {
        return new BoughtTicketDTO(ticket.getTicketId(), user.getUserId(), pServicesId);
    }
}
