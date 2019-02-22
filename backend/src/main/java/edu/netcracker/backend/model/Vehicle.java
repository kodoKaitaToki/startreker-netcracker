package edu.netcracker.backend.model;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table("vehicle")
public class Vehicle {

    @PrimaryKey("vehicle_id")
    @EqualsAndHashCode.Include
    private Long vehicleId;

    @Attribute("owner_id")
    private Long ownerId;

    @Attribute("vehicle_name")
    private String vehicleName;

    @Attribute("vehicle_seats")
    private Integer vehicleSeats;

    private List<Trip> vehicleTrips = new ArrayList<>();
}
