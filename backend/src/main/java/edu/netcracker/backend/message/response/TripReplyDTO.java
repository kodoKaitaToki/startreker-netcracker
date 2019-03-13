package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.TripReply;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripReplyDTO {

    @JsonProperty("trip_id")
    private Long tripId;
    @JsonProperty("writer_id")
    private Integer writerId;
    @JsonProperty("writer_name")
    private String writerName;
    @JsonProperty("reply_text")
    private String replyText;
    @JsonProperty("creation_date")
    private String creationDate;

    public static TripReplyDTO from(TripReply tripReply) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return TripReplyDTO.builder()
                           .tripId(tripReply.getTripId())
                           .writerId(tripReply.getWriterId())
                           .writerName(tripReply.getWriter() != null ? tripReply.getWriter()
                                                                                .getUsername() : null)
                           .replyText(tripReply.getReportText())
                           .creationDate(tripReply.getCreationDate()
                                                  .format(formatter))
                           .build();
    }
}
