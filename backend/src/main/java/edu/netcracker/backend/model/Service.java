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
@Table("service")
public class Service {

    @PrimaryKey("service_id")
    @EqualsAndHashCode.Include
    private Long serviceId;

    @Attribute("service_name")
    private String serviceName;

    @Attribute("service_description")
    private String serviceDescription;

    private Integer servicePrice;
}
