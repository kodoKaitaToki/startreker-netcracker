package edu.netcracker.backend.model;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;
import edu.netcracker.backend.utils.TripStatus;
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

    @Attribute("trip_status")
    private Integer tripStatus;

    @Attribute("approver_id")
    private Integer approverId;

    @Attribute("carrier_id")
    private Long carrierId;

    @Attribute("creation_date")
    private LocalDateTime creationDate;

    @Attribute("departure_date")
    private LocalDateTime departureDate;

    @Attribute("arrival_date")
    private LocalDateTime arrivalDate;

    @Attribute("trip_photo")
    private String tripPhoto;

    private Planet departurePlanet;
    private Planet arrivalPlanet;

    private Spaceport departureSpaceport;
    private Spaceport arrivalSpaceport;

    private List<TicketClass> ticketClasses = new ArrayList<>();
}
