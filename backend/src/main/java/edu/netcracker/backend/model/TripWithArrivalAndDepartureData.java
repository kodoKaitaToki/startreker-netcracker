package edu.netcracker.backend.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TripWithArrivalAndDepartureData {

    private Long tripId;

    private LocalDateTime departureDate;

    private String departureSpacePort;

    private String departurePlanet;

    private LocalDateTime arrivalDate;

    private String arrivalSpacePort;

    private String arrivalPlanet;
}
