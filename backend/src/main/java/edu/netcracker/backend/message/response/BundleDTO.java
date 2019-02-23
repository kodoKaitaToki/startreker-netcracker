package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.Bundle;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class BundleDTO {

    private Integer id;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("finish_date")
    private String finishDate;

    private Integer price;

    private String description;

    @JsonProperty("photo_uri")
    private String photoUri;

    private BundleDTO(Integer id,
                      LocalDate startDate,
                      LocalDate finishDate,
                      Integer price,
                      String description,
                      String photoUri) {
        this.id = id;
        this.startDate = startDate.format(DateTimeFormatter.ofPattern("yyy-MM-dd"));
        this.finishDate = finishDate.format(DateTimeFormatter.ofPattern("yyy-MM-dd"));
        this.price = price;
        this.description = description;
        this.photoUri = photoUri;
    }

    public static BundleDTO from(Bundle bundle) {
        return new BundleDTO(
                bundle.getBundleId(),
                bundle.getStartDate(),
                bundle.getFinishDate(),
                bundle.getBundlePrice(),
                bundle.getBundleDescription(),
                bundle.getBundlePhotoUri()
        );
    }
}
