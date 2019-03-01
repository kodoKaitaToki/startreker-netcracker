package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.message.request.ServiceCreateForm;
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

    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("service_descr")
    private String serviceDescription;

    private Integer carrierId;

    @JsonProperty("service_status")
    private Integer serviceStatus;

    private ServiceDTO(String serviceName,
                       String serviceDescription,
                       Integer carrierId,
                       Integer serviceStatus) {
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.carrierId = carrierId;
        this.serviceStatus = serviceStatus;
    }

    public static ServiceDTO form(ServiceCreateForm serviceCreateForm, Integer carrierId){
        return new ServiceDTO(
                serviceCreateForm.getServiceName(),
                serviceCreateForm.getServiceDescription(),
                carrierId,
                serviceCreateForm.getServiceStatus());
    }
}
