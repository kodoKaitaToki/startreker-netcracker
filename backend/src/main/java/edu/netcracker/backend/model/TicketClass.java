package edu.netcracker.backend.model;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table("ticket_class")
public class TicketClass {

    @PrimaryKey("class_id")
    @EqualsAndHashCode.Include
    private Long classId;

    @Attribute("class_name")
    private String className;

    @Attribute("trip_id")
    private Long tripId;

    @Attribute("ticket_price")
    private Integer ticketPrice;

    @Attribute("discount_id")
    private Long discountId;

    @Attribute("class_seats")
    private Integer classSeats;
  
    private Integer itemNumber;

    private Integer remainingSeats;

    private List<Service> services = new ArrayList<>();

    private Discount discount;
}
