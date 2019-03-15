package edu.netcracker.backend.message.response.HistoryDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.history.HistoryTicket;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoryTicketDTO {

    private static final String datePattern = "yyyy-MM-dd HH:mm";

    @JsonProperty("id")
    private Integer ticketId;

    @JsonProperty("seat")
    private Integer seat;

    @JsonProperty("end_price")
    private Float endPrice;

    @JsonProperty("class_name")
    private String className;

    @JsonProperty("trip")
    private HistoryTripDTO trip;

    public static HistoryTicketDTO from(HistoryTicket historyTicket) {
        HistoryTicketDTO htd = new HistoryTicketDTO();
        htd.ticketId = historyTicket.getTicketId();
        htd.seat = historyTicket.getSeat();
        htd.endPrice = historyTicket.getEndPrice();
        htd.className = historyTicket.getClassName();
        htd.trip = HistoryTripDTO.from(historyTicket.getTrip());
        return htd;
    }
}
