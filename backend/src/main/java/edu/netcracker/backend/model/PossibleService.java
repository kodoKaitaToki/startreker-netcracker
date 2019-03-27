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
    private Long pServiceId;

    @Attribute("service_id")
    private Long serviceId;

    @Attribute("class_id")
    private Long classId;

    @Attribute("service_price")
    private Long servicePrice;

    @Attribute("p_service_status")
    private Long pServiceStatus;

    private ServiceDescr service;
}
