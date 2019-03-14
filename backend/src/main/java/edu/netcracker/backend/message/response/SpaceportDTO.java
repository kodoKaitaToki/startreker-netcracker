package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SpaceportDTO {

    public SpaceportDTO(Long spaceportId,
                        String spaceportName){
        this.setSpaceportId(spaceportId);
        this.setSpaceportName(spaceportName);
    }

    @JsonProperty("spaceport_id")
    private Long spaceportId;

    @JsonProperty("spaceport_name")
    private String spaceportName;
}
