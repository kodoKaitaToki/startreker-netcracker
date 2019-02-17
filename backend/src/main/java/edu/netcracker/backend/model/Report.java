package edu.netcracker.backend.model;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.annotations.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table("report")
public class Report {

    @PrimaryKey("report_id")
    @EqualsAndHashCode.Include
    private Integer reportId;
    @Attribute("reporter_id")
    private Long reporterId;
    @Attribute("approver_id")
    private Long approverId;
    @Attribute("trip_id")
    private Long tripId;
    @Attribute("status")
    private Integer status;
    @Attribute("report_rate")
    private Integer reportRate;
    @Attribute("report_text")
    private String reportText;
    @Attribute("creation_date")
    private LocalDateTime creationDate;
}
