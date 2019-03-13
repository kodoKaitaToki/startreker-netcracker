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

    private final String findPerPeriod = "SELECT * FROM spaceport WHERE creation_date BETWEEN ? AND ?";

    @Override
    public List<Spaceport> findPerPeriod(LocalDateTime from, LocalDateTime to) {
        List<Spaceport> spaceports = new ArrayList<>();

        spaceports.addAll(getJdbcTemplate().query(
                findPerPeriod,
                new Object[]{from, to},
                getGenericMapper()));

        return spaceports;
    }
}
