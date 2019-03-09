package edu.netcracker.backend.model;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table("spaceport")
public class Spaceport {
    @PrimaryKey("spaceport_id")
    @EqualsAndHashCode.Include
    private Long spaceportId;

    @Attribute("spaceport_name")
    private String spaceportName;

    @Attribute("creation_date")
    private LocalDate creationDate;

    @Attribute("planet_id")
    private Long planetId;

    private Planet planet;
}
