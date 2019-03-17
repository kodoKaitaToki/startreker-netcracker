package edu.netcracker.backend.message.request;

import edu.netcracker.backend.model.Spaceport;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(of = "tripID")
public class PendingActivationTrip {

    private String carrierName;

    private String carrierEmail;

    private String carrierTel;

    private String approverName;

    private String approverEmail;

    private String approverTel;

    private Integer tripID;

    private Integer tripStatus;

    private String arrivalDate;

    private String departureDate;

    private String creationDate;

    private String departmentSpaceportName;

    private String departmentPlanetName;

    private String arrivalPlanetName;

    private String arrivalSpaceportName;
}
