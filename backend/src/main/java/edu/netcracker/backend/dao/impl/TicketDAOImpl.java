package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.TicketDAO;
import edu.netcracker.backend.model.Ticket;
import edu.netcracker.backend.model.User;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final String CREATE_EMPTY_TICKET_FOR_TICKET_CLASS = "INSERT INTO ticket (class_id, seat) VALUES (?, ?)";

    private final String FIND_REMAINING_SEATS = "SELECT COUNT(ticket_id) FROM ticket WHERE class_id = ? AND passenger_id IS NULL";

    private final String DELETE_ALL_TICKETS_OF_TICKET_CLASS = "DELETE FROM ticket WHERE class_id = ?";

    private static final Logger logger = LoggerFactory.getLogger(TicketDAOImpl.class);


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

    /**
     * Method for deleting all tickets of specified ticket class
     *
     * @param id - id of ticket class for which tickets have to be deleted
     */
    @Override
    public void deleteAllTicketsOfTicketClass(Long id) {
        getJdbcTemplate().update(DELETE_ALL_TICKETS_OF_TICKET_CLASS, id);
    }

    /**
     * Method for adding to database empty tickets (tickets which are not purchased)
     *
     * @param classId - id of ticket class for which tickets are created
     * @param seat    - seat number
     */
    @Override
    public void createEmptyTicketForTicketClass(Long classId, Long seat) {
        logger.debug("Adding to database empty ticket with seat number {} for ticket class with id {}", seat, classId);
        getJdbcTemplate().update(CREATE_EMPTY_TICKET_FOR_TICKET_CLASS, classId, seat);
    }

    /**
     * Method for getting amount of remaining seats for specified ticket class
     *
     * @param classId - id of ticket class for which remaining seats have to be found
     * @return - number of remaining seats
    **/
    @Override
    public Integer getRemainingSeatsForClass(Long classId) {
        logger.debug("Getting amount of remaining seats for ticket class with id {}", classId);
        return getJdbcTemplate().queryForObject(FIND_REMAINING_SEATS, new Object[]{classId}, Integer.class);
    }
}
