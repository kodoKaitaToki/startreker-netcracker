package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.Suggestion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SuggestionDTO {
    private Long id;

    @JsonProperty("class_id")
    private Long classId;

    @JsonProperty("discount_id")
    private Long discountId;

    private List<Long> pServiceIds;

    private SuggestionDTO(Long id,
                          Long classId,
                          Long discountId,
                          List<Long> pServiceIds) {

        this.id = id;
        this.classId = classId;
        this.discountId = discountId;
        this.pServiceIds = pServiceIds;
    }

    public static SuggestionDTO from(Suggestion suggestion, List<Long> pServiceIds) {
        return new SuggestionDTO(
                suggestion.getSuggestionId(),
                suggestion.getClassId(),
                suggestion.getDiscountId(),
                pServiceIds);
    }
}
