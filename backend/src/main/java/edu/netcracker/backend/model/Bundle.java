package edu.netcracker.backend.model;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table("bundle")
public class Bundle {

    @PrimaryKey("bundle_id")
    @EqualsAndHashCode.Include
    private Long bundleId;

    @Attribute("start_date")
    private LocalDate startDate;

    @Attribute("finish_date")
    private LocalDate finishDate;

    @Attribute("bundle_price")
    private Integer bundlePrice;

    @Attribute("bundle_description")
    private String bundleDescription;

    @Attribute("bundle_photo")
    private String bundlePhotoUri;

    private List<Trip> bundleTrips = new ArrayList<>();

    private List<Service> bundleServices = new ArrayList<>();

}
