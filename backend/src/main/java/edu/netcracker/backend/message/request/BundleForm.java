package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.Service;
import edu.netcracker.backend.model.TicketClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BundleForm {

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
}
