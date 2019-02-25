package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripDistributionElement {

    @JsonProperty("departure_id")
    private Long departureId;
    @JsonProperty("arrival_id")
    private Long arrivalId;
    @JsonProperty("departure_planet_id")
    private Long departurePlanetId;
    @JsonProperty("arrival_planet_id")
    private Long arrivalPlanetId;
    @JsonProperty("departure_spaceport_name")
    private String departureSpaceportName;
    @JsonProperty("arrival_spaceport_name")
    private String arrivalSpaceportName;
    @JsonProperty("departure_planet_name")
    private String departurePlanetName;
    @JsonProperty("arrival_planet_name")
    private String arrivalPlanetName;
    @JsonProperty("occurrence_count")
    private Long occurrenceCount;
    @JsonProperty("percentage")
    private Double percentage;
}
