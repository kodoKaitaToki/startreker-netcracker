package edu.netcracker.backend.message.request;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "tripID", callSuper = false)
public class TripPendingActivationDto extends GeneralPendingDto {

    private Integer tripID;

    private Integer tripStatus;

    private String arrivalDate;

    private String departureDate;

    private String creationDate;

    private String departmentSpaceportName;

    private String departmentPlanetName;

    private String arrivalPlanetName;

    private String arrivalSpaceportName;

    @Builder
    public TripPendingActivationDto(String carrierName,
                                    String carrierEmail,
                                    String carrierTel,
                                    String approverName,
                                    String approverEmail,
                                    String approverTel,
                                    Integer tripID,
                                    Integer tripStatus,
                                    String arrivalDate,
                                    String departureDate,
                                    String creationDate,
                                    String departmentSpaceportName,
                                    String departmentPlanetName,
                                    String arrivalPlanetName,
                                    String arrivalSpaceportName) {
        super(carrierName, carrierEmail, carrierTel, approverName, approverEmail, approverTel);
        this.tripID = tripID;
        this.tripStatus = tripStatus;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.creationDate = creationDate;
        this.departmentSpaceportName = departmentSpaceportName;
        this.departmentPlanetName = departmentPlanetName;
        this.arrivalPlanetName = arrivalPlanetName;
        this.arrivalSpaceportName = arrivalSpaceportName;
    }
}
