package edu.netcracker.backend.model;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Table("discount")
public class Discount {

    @PrimaryKey("discount_id")
    @EqualsAndHashCode.Include
    private Integer discountId;

    @Attribute("start_date")
    private LocalDate startDate;

    @Attribute("finish_date")
    private LocalDate finishDate;

    @Attribute("discount_rate")
    private Integer discountRate;

    @Attribute("discount_type")
    private Boolean discountType;

}
