package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @JsonProperty("departure_time")
    private String departureTime;

    @JsonProperty("arrival_date")
    private String arrivalDate;

    @JsonProperty("arrival_time")
    private String arrivalTime;


    public LocalDateTime getDepartureDateTime() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        return LocalDateTime.parse(this.departureDate + " " + this.departureTime, dateFormatter);
    }

    public LocalDateTime getArrivalDateTime() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        return LocalDateTime.parse(this.arrivalDate + " " + this.arrivalTime, dateFormatter);
    }

}
