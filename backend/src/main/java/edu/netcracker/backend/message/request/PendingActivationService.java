package edu.netcracker.backend.message.request;

import edu.netcracker.backend.model.User;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(of = "serviceId")
public class PendingActivationService {

    private String carrierName;

    private String carrierEmail;

    private String carrierTel;

    private String approverName;

    private String approverEmail;

    private String approverTel;

    private Integer serviceId;

    private String serviceName;

    private String serviceDescr;

    private String serviceStatus;

    private String creationDate;
}
