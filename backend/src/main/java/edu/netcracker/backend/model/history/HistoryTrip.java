package edu.netcracker.backend.model.history;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryTrip {
    private LocalDateTime departureDate;

    private LocalDateTime arrivalDate;

    private String departureSpaceportName;

    private String arrivalSpaceportName;

    private String departurePlanetName;

    private String arrivalPlanetName;

    private String carrierName;
}
