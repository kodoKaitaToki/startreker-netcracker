package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.Trip;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TripDTO {

    @JsonProperty("trip_id")
    private Long tripId;

    @JsonProperty("trip_status")
    private Integer status;

    @JsonProperty("trip_reply")
    @Length(min = 2, max = 5000)
    private String reply;

    public static TripDTO from(Trip trip){
        TripDTO dto = new TripDTO();
        dto.tripId = trip.getTripId();
        dto.status = trip.getTripState().getValue();
        return dto;
    }
}
