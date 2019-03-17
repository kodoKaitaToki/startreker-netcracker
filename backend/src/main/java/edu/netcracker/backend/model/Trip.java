package edu.netcracker.backend.model;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;
import edu.netcracker.backend.message.response.TripDTO;
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

    @Attribute("arrival_date")
    private LocalDateTime arrivalDate;

    @Attribute("trip_photo")
    private String tripPhoto;

    private Spaceport departurePort;

    private Spaceport arrivalPort;

    private TripState tripState;

    private User owner;

    private User approver;

    private Planet departurePlanet;
    private Planet arrivalPlanet;

    private Spaceport departureSpaceport;
    private Spaceport arrivalSpaceport;

    private List<TicketClass> ticketClasses = new ArrayList<>();

    public List<TicketClass> getTicketClasses() {
        return ticketClasses;
    }

    public void setTicketClasses(List<TicketClass> ticketClasses) {
        this.ticketClasses = ticketClasses;
    }

    public boolean changeStatus(User requestUser, TripState newTripState, TripDTO tripDTO) {

        if (requestUser == null || newTripState == null || !newTripState.isStateChangeAllowed(this,
                                                                                              requestUser,
                                                                                              this.tripState)) {

            return false;
        }

        if (newTripState.apply(this, requestUser, this.tripState, tripDTO)) {
            this.tripState = newTripState;
            return true;
        }

        return false;
    }
}
