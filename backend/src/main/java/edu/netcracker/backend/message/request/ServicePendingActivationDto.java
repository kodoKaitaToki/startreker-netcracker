package edu.netcracker.backend.message.request;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "serviceId", callSuper = false)
public class ServicePendingActivationDto extends GeneralPendingDto {

    private Integer serviceId;

    private String serviceName;

    private String serviceDescr;

    private Integer serviceStatus;

    private String creationDate;

    @Builder
    public ServicePendingActivationDto(String carrierName,
                                       String carrierEmail,
                                       String carrierTel,
                                       String approverName,
                                       String approverEmail,
                                       String approverTel,
                                       Integer serviceId,
                                       String serviceName,
                                       String serviceDescr,
                                       Integer serviceStatus,
                                       String creationDate) {
        super(carrierName, carrierEmail, carrierTel, approverName, approverEmail, approverTel);
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceDescr = serviceDescr;
        this.serviceStatus = serviceStatus;
        this.creationDate = creationDate;
    }
}
