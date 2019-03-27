package edu.netcracker.backend.message.request.trips;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Getter
public class TripCreation {
    @JsonProperty("departure_spaceport")
    private String departureSpaceport;

    @JsonProperty("arrival_spaceport")
    private String arrivalSpaceport;

    @JsonProperty("departure_planet")
    private String departurePlanet;

    @JsonProperty("arrival_planet")
    private String arrivalPlanet;

    @JsonProperty("departure_date")
    private String departureDate;

    @JsonProperty("arrival_date")
    private String arrivalDate;

    public LocalDateTime getDepartureDateTime() {
        DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd' 'HH:mm:ss")
                                                                        .toFormatter();
        return LocalDateTime.parse(this.departureDate, dateFormatter);
    }

    public LocalDateTime getArrivalDateTime() {
        DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd' 'HH:mm:ss")
                                                                        .toFormatter();
        return LocalDateTime.parse(this.arrivalDate, dateFormatter);
    }
}
