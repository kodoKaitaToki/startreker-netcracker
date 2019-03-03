package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.model.PossibleService;
import edu.netcracker.backend.model.Service;
import lombok.*;
import org.springframework.http.HttpStatus;


@Getter
@Setter
@NoArgsConstructor
public class PossibleServiceDTO {
    private Integer id;

    @JsonProperty("service_id")
    private Integer serviceId;

    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("service_description")
    private String serviceDescription;

    @JsonProperty("class_id")
    private Integer classId;

    @JsonProperty("service_price")
    private Integer servicePrice;

    private PossibleServiceDTO(Integer id,
                               Integer serviceId,
                               String serviceName,
                               String serviceDescription,
                               Integer classId,
                               Integer servicePrice) {

        this.id = id;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.classId = classId;
        this.servicePrice = servicePrice;
    }

    public static PossibleServiceDTO from(PossibleService possibleService) {
        Service service = possibleService.getService();

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
