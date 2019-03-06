package edu.netcracker.backend.model;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;
import edu.netcracker.backend.message.request.DiscountDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Table("discount")
public class Discount {

    @PrimaryKey("discount_id")
    @EqualsAndHashCode.Include
    private Long discountId;

    @Attribute("start_date")
    private LocalDateTime startDate;

    @Attribute("finish_date")
    private LocalDateTime finishDate;

    @Attribute("discount_rate")
    private Integer discountRate;

    @Attribute("is_percent")
    private Boolean isPercent;

    public static Discount toDiscount(DiscountDTO discountDTO) {
        Discount discount = new Discount();
        discount.setDiscountRate(discountDTO.getDiscountRate());
        discount.setIsPercent(discountDTO.getIsPercent());
        discount.setStartDate(LocalDate
                .parse(discountDTO.getStartDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).atStartOfDay());
        discount.setFinishDate(LocalDate
                .parse(discountDTO.getFinishDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).atTime(LocalTime.MAX));

        return discount;
    }

}
