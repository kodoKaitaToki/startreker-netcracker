package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.SpaceportDAO;
import edu.netcracker.backend.model.Spaceport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SpaceportDAOImpl extends CrudDAOImpl<Spaceport> implements SpaceportDAO {

    private final String FIND_BY_PERIOD_SQL = "SELECT * FROM spaceport WHERE creation_date BETWEEN ? AND ?";
    private final String FIND_BY_PLANET_SQL = "SELECT * FROM spaceport AS s " +
            "INNER JOIN planet AS p ON s.planet_id = p.planet_id " +
            "WHERE planet_name = ?";
    private final String FIND_ID_BY_SPACEPORT_NAME = "SELECT spaceport_id FROM spaceport WHERE spaceport_name = LOWER(?)";

    @Override
    public List<Spaceport> findByPlanet(String planet) {
        List<Spaceport> spaceports = new ArrayList<>();

        spaceports.addAll(getJdbcTemplate().query(
                FIND_BY_PLANET_SQL,
                new Object[]{planet},
                getGenericMapper()));

        return spaceports;
    }

    @Override
    public List<Spaceport> findPerPeriod(LocalDateTime from, LocalDateTime to) {
        List<Spaceport> spaceports = new ArrayList<>();

        spaceports.addAll(getJdbcTemplate().query(
                FIND_BY_PERIOD_SQL,
                new Object[]{from, to},
                getGenericMapper()));

        return spaceports;
    }

    @Override
    public Long getIdBySpaceportName(String planet) {
        return getJdbcTemplate().queryForObject(FIND_ID_BY_SPACEPORT_NAME, new Object[]{planet}, Long.class);
    }
}
