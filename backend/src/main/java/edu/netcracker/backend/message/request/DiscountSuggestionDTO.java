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
    private DiscountDTO discountDTO;

    public static DiscountSuggestionDTO toDiscountSuggestionDTO(Suggestion suggestion, DiscountDTO discountDTO) {
        DiscountSuggestionDTO simpleSuggestionDTO = new DiscountSuggestionDTO();
        simpleSuggestionDTO.setClassId(suggestion.getClassId());
        simpleSuggestionDTO.setSuggestionId(suggestion.getSuggestionId());
        simpleSuggestionDTO.setDiscountDTO(discountDTO);

        return simpleSuggestionDTO;
    }
}
