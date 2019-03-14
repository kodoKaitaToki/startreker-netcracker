package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.message.response.SpaceportDTO;
import edu.netcracker.backend.message.response.TripReplyDTO;
import edu.netcracker.backend.message.response.TripResponse;
import edu.netcracker.backend.model.Trip;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class TripRequest {

    @JsonProperty("trip_id")
    private Long tripId;

    @JsonProperty("carrier_id")
    private Integer carrierId;

    @JsonProperty("approver_id")
    private Integer approverId;

    @JsonProperty("departure_id")
    private Long departureId;

    @JsonProperty("arrival_id")
    private Long arrivalId;

    @JsonProperty("trip_status")
    private String status;

    @JsonProperty("departure_date")
    private String departureDate;

    @JsonProperty("arrival_date")
    private String arrivalDate;

    @JsonProperty("creation_date")
    private String creationDate;

    @JsonProperty("trip_reply")
    private List<TripReplyDTO> replies;

    @JsonProperty("trip_photo")
    private String tripPhoto;


    public static TripRequest from(Trip trip) {
        TripRequest dto = new TripRequest();
        dto.tripId = trip.getTripId();
        dto.status = trip.getTripState()
                         .getName();
        dto.setCarrierId(trip.getOwner() == null
                                 ? null
                                 : trip.getOwner()
                                       .getUserId());
        dto.setApproverId(trip.getApprover() == null
                                  ? null
                                  : trip.getApprover()
                                        .getUserId());
        dto.setTripPhoto(trip.getTripPhoto());

        dto.setDepartureId(trip.getDepartureId());
        dto.setArrivalId(trip.getArrivalId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        dto.setArrivalDate(trip.getArrivalDate() != null ? trip.getArrivalDate()
                                                               .format(formatter) : null);
        dto.setDepartureDate(trip.getDepartureDate() != null ? trip.getDepartureDate()
                                                                   .format(formatter) : null);
        dto.setCreationDate(trip.getCreationDate() != null ? trip.getCreationDate()
                                                                 .format(formatter) : null);

        dto.setReplies(trip.getReplies() != null ? trip.getReplies()
                                                       .stream()
                                                       .map(TripReplyDTO::from)
                                                       .collect(Collectors.toList()) : null);
        return dto;
    }

}
