package edu.netcracker.backend.message.response.trips;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.TicketClass;
import edu.netcracker.backend.model.Trip;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class AllCarriersTripsDTO {

    @JsonProperty("trip_id")
    private Long tripId;

    @JsonProperty("departure_spaceport_name")
    private String departureSpaceportName;

    @JsonProperty("arrival_spaceport_name")
    private String arrivalSpaceportName;

    @JsonProperty("departure_planet")
    private String departurePlanet;

    @JsonProperty("arrival_planet")
    private String arrivalPlanet;

    @JsonProperty("departure_date")
    private String departureDate;

    @JsonProperty("arrival_date")
    private String arrivalDate;

    @JsonProperty("creation_date")
    private String creationDate;

    @JsonProperty("ticket_classes")
    private List<Map<String, Object>> ticketClasses;

    public static AllCarriersTripsDTO from(Trip trip) {
        AllCarriersTripsDTO dto = new AllCarriersTripsDTO();
        dto.tripId = trip.getTripId();
        dto.departureSpaceportName = trip.getDepartureSpaceport()
                                         .getSpaceportName();
        dto.arrivalSpaceportName = trip.getArrivalSpaceport()
                                       .getSpaceportName();
        dto.departurePlanet = trip.getDeparturePlanet()
                                  .getPlanetName();
        dto.arrivalPlanet = trip.getArrivalPlanet()
                                .getPlanetName();
        dto.departureDate = getStringFromDate(trip.getDepartureDate());
        dto.arrivalDate = getStringFromDate(trip.getArrivalDate());
        dto.creationDate = getStringFromDate(trip.getCreationDate());
        dto.ticketClasses = new ArrayList<>();

        for (TicketClass ticketClass : trip.getTicketClasses()) {
            Map<String, Object> tcProperties = new HashMap<>();
            tcProperties.put("class_id", ticketClass.getClassId());
            tcProperties.put("class_name", ticketClass.getClassName());
            tcProperties.put("ticket_price", ticketClass.getTicketPrice());
            tcProperties.put("class_seats", ticketClass.getClassSeats());
            dto.ticketClasses.add(tcProperties);
        }

        return dto;
    }

    private static String getStringFromDate(LocalDateTime date) {
        return date.getYear()
               + "-"
               + String.format("%02d", date.getMonthValue())
               + "-"
               + String.format("%02d", date.getDayOfMonth())
               + " "
               + String.format("%02d", date.getHour())
               + ":"
               + String.format("%02d", date.getMinute())
               + ":"
               + String.format("%02d", date.getSecond());
    }
}
