package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.Trip;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TripDTO {

    @JsonProperty("trip_id")
    @NotNull
    private Long tripId;

    @JsonProperty("trip_status")
    @NotNull
    private Integer status;

    public static TripDTO from(Trip trip){
        TripDTO dto = new TripDTO();
        dto.tripId = trip.getTripId();
        dto.status = trip.getTripState().getValue();
        return dto;
    }
}
