package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.Suggestion;
import edu.netcracker.backend.model.TicketClass;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class DiscountSuggestionDTO {

    @NotNull
    @JsonProperty("suggestion_id")
    private Long suggestionId;

    @NotNull
    @JsonProperty("class_id")
    private Long classId;

    @JsonProperty("trip_id")
    private Long tripId;

    @JsonProperty("ticket_price")
    private Integer ticketPrice;

    @JsonProperty("class_name")
    private String className;

    @JsonProperty("class_seats")
    private Integer classSeats;

    @Valid
    @JsonProperty("discount")
    private DiscountDTO discountDTO;

    @JsonProperty("service_ names")
    private List<String> serviceNames;

    public static DiscountSuggestionDTO toDiscountSuggestionDTO(Suggestion suggestion, DiscountDTO discountDTO) {
        DiscountSuggestionDTO simpleSuggestionDTO = new DiscountSuggestionDTO();
        simpleSuggestionDTO.setClassId(suggestion.getClassId());
        simpleSuggestionDTO.setSuggestionId(suggestion.getSuggestionId());
        simpleSuggestionDTO.setDiscountDTO(discountDTO);

        return simpleSuggestionDTO;
    }

    public static DiscountSuggestionDTO toDiscountSuggestionDTO(TicketClass ticketClass,
                                                                Suggestion suggestion,
                                                                DiscountDTO discountDTO,
                                                                List<String> serviceNames) {
        DiscountSuggestionDTO simpleSuggestionDTO = toDiscountSuggestionDTO(suggestion, discountDTO);
        simpleSuggestionDTO.setClassName(ticketClass.getClassName());
        simpleSuggestionDTO.setClassSeats(ticketClass.getClassSeats());
        simpleSuggestionDTO.setTicketPrice(ticketClass.getTicketPrice());
        simpleSuggestionDTO.setTripId(ticketClass.getTripId());
        simpleSuggestionDTO.setServiceNames(serviceNames);

        return simpleSuggestionDTO;
    }
}
