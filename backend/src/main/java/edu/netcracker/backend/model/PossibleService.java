package edu.netcracker.backend.model;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table("possible_service")
public class PossibleService extends Service {

    @PrimaryKey("p_service_id")
    private Integer id;

    @Attribute("class_id")
    private Integer classId;

    @Attribute("service_price")
    private Integer servicePrice;
}
