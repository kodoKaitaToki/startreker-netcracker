package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.TicketDAO;
import edu.netcracker.backend.model.Ticket;
import edu.netcracker.backend.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Log4j2
@PropertySource("classpath:sql/ticketdao.properties")
public class TicketDAOImpl extends CrudDAOImpl<Ticket> implements TicketDAO {

    @Value("${FIND_ALL_BY_CLASS}")
    private String FIND_ALL_BY_CLASS;

    @Override
    public List<Ticket> findAllByClass(Number id) {
        ArrayList<Ticket> tickets = new ArrayList<>();

        try {
            tickets.addAll(getJdbcTemplate().query(FIND_ALL_BY_CLASS, new Object[]{id}, getGenericMapper()));

        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
        }

        return tickets;
    }

    @Override
    public void buyTicket(Ticket ticket, User user) {
        ticket.setPassengerId(user.getUserId());
        update(ticket);
    }
}
