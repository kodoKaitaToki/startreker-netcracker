package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.TripWithArrivalAndDepartureData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TripWithArrivalAndDepartureDataAndTicketClassesDTO extends TripWithArrivalAndDepartureDataDTO {

    @JsonProperty("ticket_classes")
    private List<DiscountTicketClassDTO> ticketClassDTOs;

    private TripWithArrivalAndDepartureDataAndTicketClassesDTO(TripWithArrivalAndDepartureData tripWithArrivalAndDepartureData,
                                                               List<DiscountTicketClassDTO> ticketClassDTOs,
                                                               String datePattern) {
        super(tripWithArrivalAndDepartureData, datePattern);
        this.ticketClassDTOs = ticketClassDTOs;
    }

    public static TripWithArrivalAndDepartureDataDTO form(TripWithArrivalAndDepartureData tripWithArrivalAndDepartureData,
                                                          List<DiscountTicketClassDTO> ticketClassDTOs,
                                                          String datePattern) {
        return new TripWithArrivalAndDepartureDataAndTicketClassesDTO(tripWithArrivalAndDepartureData,
                                                                      ticketClassDTOs,
                                                                      datePattern);
    }
}
