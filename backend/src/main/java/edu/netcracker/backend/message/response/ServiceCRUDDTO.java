package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.ServiceDescr;
import edu.netcracker.backend.utils.ServiceStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ServiceCRUDDTO {

    private Long id;

    @JsonProperty("approver_name")
    private String approverName;

    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("service_descr")
    private String serviceDescription;

    @JsonProperty("service_status")
    private String serviceStatus;

    @JsonProperty("creation_date")
    private String creationDate;

    @JsonProperty("reply_text")
    private String replyText;

    public ServiceCRUDDTO(){}

    private ServiceCRUDDTO(Long id,
                           String approverName,
                           String serviceName,
                           String serviceDescription,
                           String serviceStatus,
                           LocalDateTime creationDate) {
        this.id = id;
        this.approverName = approverName;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.serviceStatus = serviceStatus;
        this.creationDate = creationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static ServiceCRUDDTO form(ServiceDescr serviceDescr, String approver_name) {
        int status = serviceDescr.getServiceStatus();
        String statusVal = ServiceStatus.values()[status - 1].toString();
        return new ServiceCRUDDTO(
                serviceDescr.getServiceId(),
                approver_name,
                serviceDescr.getServiceName(),
                serviceDescr.getServiceDescription(),
                statusVal,
                serviceDescr.getCreationDate());
    }
}
