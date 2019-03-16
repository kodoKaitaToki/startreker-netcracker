package edu.netcracker.backend.message.response.HistoryDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HistoryOrderDTO {

    @JsonProperty("bundle")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HistoryBundleDTO bundle;

    @JsonProperty("ticket")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HistoryTicketDTO ticket;
}
