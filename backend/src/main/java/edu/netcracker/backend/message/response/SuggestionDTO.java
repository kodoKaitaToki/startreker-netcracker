package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SuggestionDTO {
    private Integer id;

    @JsonProperty("class_id")
    private Integer classId;

    @JsonProperty("discount_id")
    private String discountId;

    private List<Integer> pServiceIds;

    private SuggestionDTO(Integer id,
                          Integer classId,
                          String discountId,
                          List<Integer> pServiceIds) {

        this.id = id;
        this.classId = classId;
        this.discountId = discountId;
        this.pServiceIds = pServiceIds;
    }

    public static SuggestionDTO from() {
        return null;
    }
}
