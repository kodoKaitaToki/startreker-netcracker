package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BundleForm {
    private static final String datePattern = "yyyy-MM-dd";

    @JsonProperty("id")
    private Long id;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("finish_date")
    private String finishDate;

    @JsonProperty("bundle_price")
    private Integer bundlePrice;

    @JsonProperty("bundle_description")
    private String bundleDescription;

    @JsonProperty("photo_uri")
    private String photoUri;

    @JsonProperty("bundle_trips")
    private List<TripForm> bundleTrips = new ArrayList<>();

}
