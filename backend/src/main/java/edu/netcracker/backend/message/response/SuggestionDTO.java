package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.Suggestion;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SuggestionDTO {
    private Integer id;

    @JsonProperty("class_id")
    private Integer classId;

    @JsonProperty("discount_id")
    private Integer discountId;

    private List<Integer> pServiceIds;

    private SuggestionDTO(Integer id,
                          Integer classId,
                          Integer discountId,
                          List<Integer> pServiceIds) {

        this.id = id;
        this.classId = classId;
        this.discountId = discountId;
        this.pServiceIds = pServiceIds;
    }

    public static SuggestionDTO from(Suggestion suggestion, List<Integer> pServiceIds) {
        return new SuggestionDTO(
                suggestion.getSuggestionId(),
                suggestion.getClassId(),
                suggestion.getDiscountId(),
                pServiceIds);
    }
}
