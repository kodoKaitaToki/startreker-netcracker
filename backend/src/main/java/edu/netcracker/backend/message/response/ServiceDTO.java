package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.message.request.ServiceCreateForm;
import edu.netcracker.backend.model.ServiceDescr;
import edu.netcracker.backend.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ServiceDTO {

    private Long id;

    @JsonProperty("approver_name")
    private String approverName;

    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("service_descr")
    private String serviceDescription;

    @JsonProperty("service_status")
    private Integer serviceStatus;

    @JsonProperty("creation_date")
    private LocalDate creationDate;

    @JsonProperty("reply_text")
    private String replyText;

    public ServiceDTO(){}

    private ServiceDTO(Long id,
                       String approverName,
                       String serviceName,
                       String serviceDescription,
                       Integer serviceStatus,
                       LocalDate creationDate) {
        this.id = id;
        this.approverName = approverName;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.serviceStatus = serviceStatus;
        this.creationDate = creationDate;
    }

    public static ServiceDTO form(ServiceDescr serviceDescr, String approver_name) {
        return new ServiceDTO(
                serviceDescr.getServiceId(),
                approver_name,
                serviceDescr.getServiceName(),
                serviceDescr.getServiceDescription(),
                serviceDescr.getServiceStatus(),
                serviceDescr.getCreationDate());
    }
}
