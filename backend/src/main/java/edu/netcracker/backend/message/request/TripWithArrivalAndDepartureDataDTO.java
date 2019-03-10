package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.TripWithArrivalAndDepartureData;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public abstract class TripWithArrivalAndDepartureDataDTO {

    @JsonProperty("trip_id")
    private Long tripId;

    @JsonProperty("departure_date")
    private String departureDate;

    @JsonProperty("departure_spaceport_name")
    private String departureSpacePort;

    @JsonProperty("departure_planet_name")
    private String departurePlanet;

    @JsonProperty("arrival_date")
    private String arrivalDate;

    @JsonProperty("arrival_spaceport_name")
    private String arrivalSpacePort;

    @JsonProperty("arrival_planet_name")
    private String arrivalPlanet;

    TripWithArrivalAndDepartureDataDTO(TripWithArrivalAndDepartureData tripWithArrivalAndDepartureData,
                                       String datePattern) {
        this.tripId = tripWithArrivalAndDepartureData.getTripId();
        this.departureDate = tripWithArrivalAndDepartureData.getDepartureDate()
                .format(DateTimeFormatter.ofPattern(datePattern));
        this.departureSpacePort = tripWithArrivalAndDepartureData.getDepartureSpacePort();
        this.departurePlanet = tripWithArrivalAndDepartureData.getDeparturePlanet();
        this.arrivalDate = tripWithArrivalAndDepartureData.getArrivalDate()
                .format(DateTimeFormatter.ofPattern(datePattern));
        this.arrivalSpacePort = tripWithArrivalAndDepartureData.getArrivalSpacePort();
        this.arrivalPlanet = tripWithArrivalAndDepartureData.getArrivalPlanet();
    }
}
