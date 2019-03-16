package edu.netcracker.backend.message.response.HistoryDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

//This class is a stub. It will be redesigned based on FE requirements
public class HistoryServiceDTO {

    @JsonProperty("bought_services_name")
    private String boughtServicesName;

    public static HistoryServiceDTO from(String serviceName) {
        HistoryServiceDTO hsd = new HistoryServiceDTO();
        hsd.boughtServicesName = serviceName;
        return hsd;
    }
}
