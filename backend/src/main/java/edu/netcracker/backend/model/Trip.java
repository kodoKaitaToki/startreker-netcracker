package edu.netcracker.backend.model;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;
import edu.netcracker.backend.model.state.trip.TripState;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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

    @Attribute("creation_date")
    private LocalDateTime creationDate;

    @Attribute("departure_date")
    private LocalDateTime departureDate;

    @Attribute("departure_id")
    private Long departureId;

    @Attribute("arrival_date")
    private LocalDateTime arrivalDate;

    @Attribute("arrival_id")
    private Long arrivalId;

    @Attribute("trip_photo")
    private String tripPhoto;

    private Spaceport departurePort;

    private Spaceport arrivalPort;

    private TripState tripState;

    private User owner;

    private User approver;

    private Spaceport departureSpaceport;

    private Spaceport arrivalSpaceport;


    private List<TripReply> replies;

    private List<TicketClass> ticketClasses = new ArrayList<>();
}
