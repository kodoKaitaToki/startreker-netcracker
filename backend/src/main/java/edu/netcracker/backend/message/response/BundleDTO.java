package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.Bundle;
import edu.netcracker.backend.model.Service;
import edu.netcracker.backend.model.TicketClass;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class BundleDTO {

    @JsonProperty("id")
    private Integer id;

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

    @JsonProperty("bundle_tickets")
    private List<TicketClass> bundleTickets;

    @JsonProperty("bundle_services")
    private List<Service> bundleServices;

    private BundleDTO(Integer id,
                      LocalDate startDate,
                      LocalDate finishDate,
                      Integer price,
                      String description,
                      String photoUri,
                      List<TicketClass> bundleTickets,
                      List<Service> bundleServices
    ) {
        this.id = id;
        this.startDate = startDate.format(DateTimeFormatter.ofPattern("yyy-MM-dd"));
        this.finishDate = finishDate.format(DateTimeFormatter.ofPattern("yyy-MM-dd"));
        this.bundlePrice = price;
        this.bundleDescription = description;
        this.photoUri = photoUri;
        this.bundleTickets = bundleTickets;
        this.bundleServices = bundleServices;
    }

    public static BundleDTO from(Bundle bundle) {
        return new BundleDTO(
                bundle.getBundleId(),
                bundle.getStartDate(),
                bundle.getFinishDate(),
                bundle.getBundlePrice(),
                bundle.getBundleDescription(),
                bundle.getBundlePhotoUri(),
                bundle.getBundleTickets(),
                bundle.getBundleServices()
        );
    }
}
