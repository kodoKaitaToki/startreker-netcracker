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
@Table("service_reply")
public class ServiceReply {

    @Attribute("service_id")
    @EqualsAndHashCode.Include
    private Integer serviceId;
    @Attribute("writer_id")
    private Long writerId;
    @Attribute("reply_text")
    private String reportText;
    @Attribute("creation_date")
    private LocalDateTime creationDate;
}