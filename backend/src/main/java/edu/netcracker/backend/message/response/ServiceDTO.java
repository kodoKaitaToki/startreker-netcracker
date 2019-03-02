package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.message.request.ServiceCreateForm;
import edu.netcracker.backend.model.ServiceDescr;
import edu.netcracker.backend.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ServiceDTO {

    private Long id;

    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("service_descr")
    private String serviceDescription;

    @JsonProperty("service_status")
    private Integer serviceStatus;

    private ServiceDTO(Long id,
                       String serviceName,
                       String serviceDescription,
                       Integer serviceStatus) {
        this.id = id;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.serviceStatus = serviceStatus;
    }

    public static ServiceDTO form(ServiceDescr serviceDescr) {
        return new ServiceDTO(
                serviceDescr.getServiceId(),
                serviceDescr.getServiceName(),
                serviceDescr.getServiceDescription(),
                serviceDescr.getServiceStatus());
    }
}
