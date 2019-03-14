package edu.netcracker.backend.model;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table("ticket")
public class Ticket {
    @PrimaryKey("ticket_id")
    @EqualsAndHashCode.Include
    private Integer ticketId;

    @Attribute("passenger_id")
    private Integer passengerId;

    @Attribute("class_id")
    private Integer classId;

    @Attribute("seat")
    private Integer seat;

    @Attribute("end_price")
    private Float endPrice;

    @Attribute("purchase_date")
    private LocalDateTime purchaseDate;
}
