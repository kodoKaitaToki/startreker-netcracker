package edu.netcracker.backend.model;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

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

    @Attribute("planet_name")
    private String planetName;
}
