package edu.netcracker.backend.message.response.HistoryDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.history.HistoryTicket;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class HistoryTicketDTO {

    private static final String datePattern = "yyyy-MM-dd HH:mm";

    @JsonProperty("id")
    private Integer ticketId;

    @JsonProperty("seat")
    private Integer seat;

    @JsonProperty("end_price")
    private Float endPrice;

    @JsonProperty("purchase_date")
    private String purchaseDate;

    @JsonProperty("class_name")
    private String className;

    @JsonProperty("trip")
    private HistoryTripDTO trip;

    @JsonProperty("bundle_id")
    private Integer bundleId;

    public static HistoryTicketDTO from(HistoryTicket historyTicket) {
        HistoryTicketDTO htd = new HistoryTicketDTO();
        htd.ticketId = historyTicket.getTicketId();
        htd.seat = historyTicket.getSeat();
        htd.endPrice = historyTicket.getEndPrice();
        htd.purchaseDate = historyTicket.getPurchaseDate()
                                        .format(DateTimeFormatter.ofPattern(datePattern));
        htd.className = historyTicket.getClassName();
        htd.bundleId = historyTicket.getBundleId();
        htd.trip = HistoryTripDTO.from(historyTicket.getTrip());
        return htd;
    }
}
