package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripDistributionElement {

    @JsonProperty("departure_planet_id")
    private Long departurePlanetId;
    @JsonProperty("arrival_planet_id")
    private Long arrivalPlanetId;
    @JsonProperty("departure_planet_name")
    private String departurePlanetName;
    @JsonProperty("arrival_planet_name")
    private String arrivalPlanetName;
    @JsonProperty("occurrence_count")
    private Long occurrenceCount;
    @JsonProperty("percentage")
    private Double percentage;
}
