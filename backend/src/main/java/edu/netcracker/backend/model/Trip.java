package edu.netcracker.backend.model;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table("trip")
public class Trip {

    @PrimaryKey("trip_id")
    @EqualsAndHashCode.Include
    private Long tripId;

    @Attribute("vehicle_id")
    private Long vehicleId;

    @Attribute("trip_status")
    private Integer tripStatus;

    @Attribute("creation_date")
    private LocalDate creationDate;

    @Attribute("departure_date")
    private LocalDateTime departureDate;

    @Attribute("arrival_date")
    private LocalDateTime arrivalDate;

    @Attribute("departure_id")
    private Long departureId;

    @Attribute("arrival_id")
    private Long arrivalId;

    @Attribute("approver_id")
    private Long approverId;

    @Attribute("trip_photo")
    private Image tripPhoto;

    private List<TicketClass> ticketClasses = new ArrayList<>();

    public Trip(LocalDateTime departureDate, LocalDateTime arrivalDate, Long departureId, Long arrivalId) {
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.departureId = departureId;
        this.arrivalId = arrivalId;
    }

    public Long getTripId() {
        return tripId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public Integer getTripStatus() {
        return tripStatus;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    public Long getDepartureId() {
        return departureId;
    }

    public Long getArrivalId() {
        return arrivalId;
    }

    public Long getApproverId() {
        return approverId;
    }

    public Image getTripPhoto() {
        return tripPhoto;
    }

    public List<TicketClass> getTicketClasses() {
        return ticketClasses;
    }

    public void setTicketClasses(List<TicketClass> ticketClasses) {
        this.ticketClasses = ticketClasses;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setTripStatus(Integer tripStatus) {
        this.tripStatus = tripStatus;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public void setArrivalDate(LocalDateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public void setDepartureId(Long departureId) {
        this.departureId = departureId;
    }

    public void setArrivalId(Long arrivalId) {
        this.arrivalId = arrivalId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public void setTripPhoto(Image tripPhoto) {
        this.tripPhoto = tripPhoto;
    }
}
