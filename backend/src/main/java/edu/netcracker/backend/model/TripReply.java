package edu.netcracker.backend.model;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table("trip_reply")
public class TripReply {

    @Attribute("trip_id")
    @EqualsAndHashCode.Include
    private Long tripId;
    @Attribute("writer_id")
    private Integer writerId;
    @Attribute("reply_text")
    private String reportText;
    @Attribute("creation_date")
    private LocalDateTime creationDate;

    private User writer;
}