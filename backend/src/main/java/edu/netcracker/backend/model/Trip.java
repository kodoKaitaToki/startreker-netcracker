package edu.netcracker.backend.model;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table("trip")
public class Trip {

    @PrimaryKey("trip_id")
    @EqualsAndHashCode.Include
    private Long tripId;

    @Attribute("carrier_id")
    private Integer carrierId;

    @Attribute("trip_status")
    private Integer tripStatus;

    @Attribute("departure_date")
    private LocalDateTime departureDate;

    @Attribute("arrival_date")
    private LocalDateTime arrivalDate;

    @Attribute("arrival_id")
    private Integer arrivalId;

    @Attribute("trip_photo")
    private String tripPhoto;

    @Attribute("approver_id")
    private Integer approverId;

    @Attribute("creation_date")
    private LocalDateTime creationDate;

    private List<TicketClass> ticketClasses = new ArrayList<>();

}
