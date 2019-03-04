package edu.netcracker.backend.model;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table("possible_service")
public class PossibleService {

    @PrimaryKey("p_service_id")
    @EqualsAndHashCode.Include
    private Integer pServiceId;

    @Attribute("service_id")
    private Integer serviceId;

    @Attribute("class_id")
    private Integer classId;

    @Attribute("service_price")
    private Integer servicePrice;

    private Service service;
}
