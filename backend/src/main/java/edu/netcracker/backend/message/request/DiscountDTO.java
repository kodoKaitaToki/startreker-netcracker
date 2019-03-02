package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.validation.annotation.DateValidation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDTO {

    @JsonProperty("discount_id")
    private Long discountId;

    @NotNull
    @JsonProperty("start_date")
    @DateValidation(message = "invalid date format")
    private String startDate;

    @NotNull
    @JsonProperty("finish_date")
    @DateValidation(message = "invalid date format")
    private String finishDate;

    @NotNull
    @JsonProperty("discount_rate")
    private Integer discountRate;

    @NotNull
    @JsonProperty("discount_type")
    private Boolean discountType;
}
