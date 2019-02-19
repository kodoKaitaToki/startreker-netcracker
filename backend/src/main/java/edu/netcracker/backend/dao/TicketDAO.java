package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Ticket;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TicketDAO extends CrudDAO<Ticket> {

    private final String findAllByClass = "SELECT * FROM ticket WHERE class_id = ?";

    public List<Ticket> findAllByClass(Number id) {
        ArrayList<Ticket> tickets = new ArrayList<>();

        try {
            tickets.addAll(getJdbcTemplate().query(
                    findAllByClass,
                    new Object[]{id},
                    getGenericMapper()));


        } catch (EmptyResultDataAccessException e) {
            logger.error(e.getMessage());
        }

        return tickets;
    }
}
