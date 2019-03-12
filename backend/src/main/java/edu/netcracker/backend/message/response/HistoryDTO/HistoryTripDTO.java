package edu.netcracker.backend.message.response.HistoryDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.history.HistoryTrip;

import java.time.format.DateTimeFormatter;

public class HistoryTripDTO {
    private static final String datePattern = "yyyy-MM-dd HH:mm";


    @JsonProperty("departure_spaceport_name")
    private String departureSpaceportName;
    @JsonProperty("arrival_spaceport_name")
    private String arrivalSpaceportName;
    @JsonProperty("departure_planet_name")
    private String departurePlanetName;
    @JsonProperty("arrival_planet_name")
    private String arrivalPlanetName;
    @JsonProperty("carrier_name")
    private String carrierName;
    @JsonProperty("departure_date")
    private String departureDate;
    @JsonProperty("arrival_date")
    private String arrivalDate;

    public static HistoryTripDTO from(HistoryTrip trip){
        HistoryTripDTO htd = new HistoryTripDTO();
        htd.departureSpaceportName = trip.getDepartureSpaceportName();
        htd.arrivalSpaceportName = trip.getArrivalSpaceportName();
        htd.departurePlanetName = trip.getDeparturePlanetName();
        htd.arrivalPlanetName = trip.getArrivalPlanetName();
        htd.carrierName = trip.getCarrierName();
        htd.departureDate = trip.getDepartureDate().format(DateTimeFormatter.ofPattern(datePattern));
        htd.arrivalDate = trip.getArrivalDate().format(DateTimeFormatter.ofPattern(datePattern));
        return htd;
    }
}
