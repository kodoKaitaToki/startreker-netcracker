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

    @Attribute("vehicle_id")
    private Long vehicleId;

    @Attribute("trip_status")
    private Integer tripStatus;

    @Attribute("creation_date")
    private LocalDate creationDate;

    @Attribute("departure_date")
    private LocalDateTime departureDate;

    private List<TicketClass> ticketClasses = new ArrayList<>();

}
