package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.netcracker.backend.model.Planet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanetDTO {

    @JsonProperty("planet_id")
    private Long planetId;

    @JsonProperty("planet_name")
    private String planetName;

    public static PlanetDTO from(Planet planet){
        return new PlanetDTO(planet.getPlanetId(), planet.getPlanetName());
    }
}
