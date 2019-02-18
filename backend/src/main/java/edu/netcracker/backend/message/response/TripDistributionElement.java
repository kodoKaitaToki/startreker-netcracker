package edu.netcracker.backend.message.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripDistributionElement {

    private Long departure_id;
    private Long arrival_id;
    private Long departure_planet_id;
    private Long arrival_planet_id;
    private String departure_spaceport_name;
    private String arrival_spaceport_name;
    private String departure_planet_name;
    private String arrival_planet_name;
    private Long occurrence_count;
    private Double percentage;
}
