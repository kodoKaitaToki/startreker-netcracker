package edu.netcracker.backend.model.history;

import edu.netcracker.backend.model.Ticket;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class HistoryTicket extends Ticket {
    private String className;

    private HistoryTrip trip;
}
