package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BundleTripDTO {

    @JsonProperty("trip_id")
    private Long tripId;

    @JsonProperty("departure_spaceport_name")
    private String departurePortName;

    @JsonProperty("arrival_spaceport_name")
    private String arrivalPortName;

    @JsonProperty("departure_planet")
    private String departurePlanetName;

    @JsonProperty("arrival_planet")
    private String arrivalPlanet;

    @JsonProperty("ticket_classes")
    private List<BundleTicketClassDTO> ticketClasses;
}
