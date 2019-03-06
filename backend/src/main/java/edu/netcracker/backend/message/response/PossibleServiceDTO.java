package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.model.PossibleService;
import edu.netcracker.backend.model.Service;
import edu.netcracker.backend.model.ServiceDescr;
import lombok.*;
import org.springframework.http.HttpStatus;


@Getter
@Setter
@NoArgsConstructor
public class PossibleServiceDTO {
    private Long id;

    @JsonProperty("service_id")
    private Long serviceId;

    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("service_description")
    private String serviceDescription;

    @JsonProperty("class_id")
    private Long classId;

    @JsonProperty("service_price")
    private Long servicePrice;

    private PossibleServiceDTO(Long id,
                               Long serviceId,
                               String serviceName,
                               String serviceDescription,
                               Long classId,
                               Long servicePrice) {

        this.id = id;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.classId = classId;
        this.servicePrice = servicePrice;
    }

    public static PossibleServiceDTO from(PossibleService possibleService) {
        ServiceDescr service = possibleService.getService();

        if (service != null)
            return new PossibleServiceDTO(
                possibleService.getPServiceId(),
                possibleService.getServiceId(),
                possibleService.getService().getServiceName(),
                possibleService.getService().getServiceDescription(),
                possibleService.getClassId(),
                possibleService.getServicePrice());
        else
            throw new  RequestException("Attached service with it " + possibleService.getServiceId() + " not found",
                    HttpStatus.NOT_FOUND);
    }

}
