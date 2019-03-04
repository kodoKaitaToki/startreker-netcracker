package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.Suggestion;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DiscountSuggestionDTO {

    @NotNull
    @JsonProperty("suggestion_id")
    private Long suggestionId;

    @NotNull
    @JsonProperty("class_id")
    private Long classId;

    @NotNull
    @Valid
    @JsonProperty("discount")
    private DiscountDTO discount;

    public static DiscountSuggestionDTO toSimpleSuggestionDTO(Suggestion suggestion, String datePattern) {
        DiscountSuggestionDTO simpleSuggestionDTO = new DiscountSuggestionDTO();
        simpleSuggestionDTO.setClassId(suggestion.getClassId());
        simpleSuggestionDTO.setSuggestionId(suggestion.getSuggestionId());
        simpleSuggestionDTO.setDiscount(DiscountDTO.toDiscountDTO(suggestion.getDiscount(), datePattern));

        return simpleSuggestionDTO;
    }
}
