package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.model.Planet;
import edu.netcracker.backend.model.Spaceport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SpaceportMapper implements RowMapper<Spaceport> {

    private final RowMapper<Planet> planetMapper;
    private String parameterPrefix = "";

    @Autowired
    public SpaceportMapper(RowMapper<Planet> planetMapper) {
        this.planetMapper = planetMapper;
    }

    @Override
    public Spaceport mapRow(ResultSet resultSet, int i) throws SQLException {
        Spaceport sp = new Spaceport();
        sp.setSpaceportId(resultSet.getLong("spaceport_id"));
        sp.setSpaceportName(resultSet.getString("spaceport_name"));
        sp.setCreationDate(resultSet.getTimestamp("creation_date")
                                    .toLocalDateTime());
        sp.setPlanet(planetMapper.mapRow(resultSet, i));

        //Mapping id for compatibility
        sp.setPlanetId(sp.getPlanetId());
        return sp;
    }
}
