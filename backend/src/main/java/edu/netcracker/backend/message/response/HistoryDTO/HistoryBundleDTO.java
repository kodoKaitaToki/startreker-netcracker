package edu.netcracker.backend.message.response.HistoryDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HistoryBundleDTO {
    @JsonProperty("bundle_id")
    private Integer bundleId;

    @JsonProperty("purchase_date")
    private String purchaseDate;

    @JsonProperty("end_price")
    private Float endPrice;
}
