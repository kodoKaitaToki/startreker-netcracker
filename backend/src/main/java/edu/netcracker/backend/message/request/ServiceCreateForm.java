package edu.netcracker.backend.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class ServiceCreateForm extends UserForm {

    @JsonProperty("approver_name")
    private String approverName;

    @NotBlank
    @JsonProperty("service_name")
    @Size(min = 3)
    private String serviceName;

    @NotBlank
    @JsonProperty("service_descr")
    @Size(min = 3)
    private String serviceDescription;

    @NotBlank
    @JsonProperty("service_status")
    private Integer serviceStatus;

    @JsonProperty("creation_date")
    private LocalDate creationDate;
}
