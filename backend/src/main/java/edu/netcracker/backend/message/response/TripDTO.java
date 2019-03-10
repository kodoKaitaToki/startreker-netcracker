package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.Trip;
import lombok.Getter;

@Getter
public class TripDTO {

    @JsonProperty("trip_id")
    private Long tripId;
    @JsonProperty("trip_status")
    private Integer status;

    private TripDTO(){}

    public static TripDTO from(Trip trip){
        TripDTO dto = new TripDTO();
        dto.tripId = trip.getTripId();
        dto.status = trip.getTripStatus();
        return dto;
    }
}
