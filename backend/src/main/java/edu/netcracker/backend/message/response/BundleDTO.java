package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class BundleDTO {
    private static final String datePattern = "yyyy-MM-dd HH:mm";

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
    private List<BundleTripDTO> bundleTrips;

    public static LocalDateTime convertToLocalDate(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(datePattern));
    }

    public static String convertToString(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(datePattern));
    }

}
