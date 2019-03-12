package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.Spaceport;
import edu.netcracker.backend.model.Trip;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class TripResponse {

    @JsonProperty("trip_id")
    private Long tripId;

    @JsonProperty("carrier_id")
    private Integer carrierId;

    @JsonProperty("carrier_name")
    private String carrierName;

    @JsonProperty("approver_id")
    private Integer approverId;

    @JsonProperty("departure_spaceport")
    private SpaceportDTO departureSpaceport;

    @JsonProperty("arrival_spaceport")
    private SpaceportDTO arrivalSpaceport;

    @JsonProperty("trip_status")
    private Integer status;

    @JsonProperty("departure_date")
    private String departureDate;

    @JsonProperty("arrival_date")
    private String arrivalDate;

    @JsonProperty("creation_date")
    private String creationDate;

    @JsonProperty("trip_reply")
    @Length(min = 2, max = 5000)
    private List<TripReplyDTO> replies;

    @JsonProperty("trip_photo")
    private String tripPhoto;

    public static TripResponse from(Trip trip) {
        TripResponse dto = new TripResponse();
        dto.tripId = trip.getTripId();
        dto.status = trip.getTripState()
                         .getDatabaseValue();
        dto.setCarrierId(trip.getOwner() == null
                                 ? null
                                 : trip.getOwner()
                                       .getUserId());
        dto.setApproverId(trip.getApprover() == null
                                  ? null
                                  : trip.getApprover()
                                        .getUserId());
        dto.setTripPhoto(trip.getTripPhoto());

        dto.setCarrierName(trip.getOwner().getUsername());

        dto.setDepartureSpaceport(SpaceportDTO.from(trip.getDepartureSpaceport()));
        dto.setArrivalSpaceport(SpaceportDTO.from(trip.getArrivalSpaceport()));

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
