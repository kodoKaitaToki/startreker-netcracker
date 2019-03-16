package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.model.Planet;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PlanetMapper implements RowMapper<Planet> {

    private String parameterPrefix = "";

    @Override
    public Planet mapRow(ResultSet rs, int i) throws SQLException {
        Planet p = new Planet();
        p.setPlanetId(rs.getLong(parameterPrefix + "planet_id"));
        p.setPlanetName(rs.getString(parameterPrefix + "planet_name"));
        return p;
    }
}
