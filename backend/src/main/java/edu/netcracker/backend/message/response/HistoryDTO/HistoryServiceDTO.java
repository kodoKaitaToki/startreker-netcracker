package edu.netcracker.backend.message.response.HistoryDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.history.HistoryService;

//This class is a stub. It will be redesigned based on FE requirements
public class HistoryServiceDTO {

    @JsonProperty("bought_services_name")
    private String boughtServicesName;

    @JsonProperty("bought_services_count")
    private Number boughtServicesCount;

    public static HistoryServiceDTO from(HistoryService service) {
        HistoryServiceDTO hsd = new HistoryServiceDTO();
        hsd.boughtServicesName = service.getServiceName();
        hsd.boughtServicesCount = service.getServiceCount();
        return hsd;
    }
}
