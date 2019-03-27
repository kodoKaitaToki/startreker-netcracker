package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.TripWithArrivalAndDepartureData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TripWithArrivalAndDepartureDataAndSuggestionsDTO extends TripWithArrivalAndDepartureDataDTO {

    @JsonProperty("suggestion")
    private List<DiscountSuggestionDTO> suggestionDTOs;

    private TripWithArrivalAndDepartureDataAndSuggestionsDTO(TripWithArrivalAndDepartureData tripWithArrivalAndDepartureData,
                                                             List<DiscountSuggestionDTO> suggestionDTOs,
                                                             String datePattern) {
        super(tripWithArrivalAndDepartureData, datePattern);
        this.suggestionDTOs = suggestionDTOs;
    }

    public static TripWithArrivalAndDepartureDataDTO form(TripWithArrivalAndDepartureData tripWithArrivalAndDepartureData,
                                                          List<DiscountSuggestionDTO> suggestionDTOs,
                                                          String datePattern) {
        return new TripWithArrivalAndDepartureDataAndSuggestionsDTO(tripWithArrivalAndDepartureData,
                                                                    suggestionDTOs,
                                                                    datePattern);
    }
}
