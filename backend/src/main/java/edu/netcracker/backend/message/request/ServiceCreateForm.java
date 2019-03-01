package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ServiceCreateForm extends UserForm {

    @NotBlank
    @JsonProperty("service_name")
    @Size(min = 3, max = 50)
    private String serviceName;

    @NotBlank
    @JsonProperty("service_descr")
    private String serviceDescription;

    @NotBlank
    @JsonProperty("service_status")
    private Integer serviceStatus;

}
