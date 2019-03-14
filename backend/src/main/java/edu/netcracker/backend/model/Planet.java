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
@Table("planet")
public class Planet {
    @PrimaryKey("planet_id")
    @EqualsAndHashCode.Include
    private Long planetId;

    @Attribute("planet_name")
    private String planetName;
}
