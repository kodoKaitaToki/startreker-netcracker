package edu.netcracker.backend.model;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @Attribute("discount_type")
    private Boolean discountType;

}
