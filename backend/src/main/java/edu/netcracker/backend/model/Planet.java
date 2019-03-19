package edu.netcracker.backend.model;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Table("planet")
public class Planet {
    @PrimaryKey("planet_id")
    @EqualsAndHashCode.Include
    private Long planetId;

    @Attribute("planetName")
    private String planetName;
}
