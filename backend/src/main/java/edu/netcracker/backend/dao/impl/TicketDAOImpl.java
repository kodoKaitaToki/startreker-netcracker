package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.TicketDAO;
import edu.netcracker.backend.model.Ticket;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TicketDAOImpl extends CrudDAO<Ticket> implements TicketDAO {

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
