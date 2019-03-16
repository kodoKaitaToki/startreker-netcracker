package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.TicketDAO;
import edu.netcracker.backend.dao.mapper.history.HistoryTicketMapper;
import edu.netcracker.backend.model.history.HistoryTicket;
import edu.netcracker.backend.model.Ticket;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@Log4j2
@PropertySource("classpath:sql/ticketdao.properties")
public class TicketDAOImpl extends CrudDAOImpl<Ticket> implements TicketDAO {

    public TicketDAOImpl(NamedParameterJdbcTemplate namedTemplate) {
        this.namedTemplate = namedTemplate;
    }

    private final Logger logger = LoggerFactory.getLogger(TicketDAOImpl.class);

    private NamedParameterJdbcTemplate namedTemplate;

    @Value("${FIND_ALL_BY_CLASS}")
    private String FIND_ALL_BY_CLASS;

    private final static String FIND_ALL_BY_USER = "SELECT t.ticket_id, t.seat, t.end_price, t.purchase_date, "
                                                   + "tc.class_name, tr.departure_date, tr.arrival_date, "
                                                   + "sd.spaceport_name, sa.spaceport_name, pa.planet_name, "
                                                   + "pd.planet_name, u.user_name, t.b_bundle "
                                                   + "FROM ticket t "
                                                   + "JOIN ticket_class tc ON tc.class_id = t.class_id "
                                                   + "JOIN trip tr ON tr.trip_id = tc.trip_id "
                                                   + "JOIN spaceport sa ON sa.spaceport_id = tr.arrival_id "
                                                   + "JOIN spaceport sd ON sd.spaceport_id = tr.departure_id "
                                                   + "JOIN planet pa ON sa.planet_id = pa.planet_id "
                                                   + "JOIN planet pd ON sd.planet_id = pd.planet_id "
                                                   + "JOIN user_a u ON tr.carrier_id = u.user_id "
                                                   + "WHERE t.passenger_id = :id "
                                                   + "AND (t.purchase_date > :start_date OR CAST(:start_date AS TIMESTAMP) IS NULL) "
                                                   + "AND (t.purchase_date < :end_date OR CAST(:end_date AS TIMESTAMP) IS NULL) "
                                                   + "ORDER BY t.purchase_date DESC "
                                                   + "LIMIT :limit "
                                                   + "OFFSET :offset";


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
    public List<HistoryTicket> findAllPurchasedByUser(Number user_id,
                                                      Number limit,
                                                      Number offset,
                                                      LocalDate startDate,
                                                      LocalDate endDate) {
        logger.debug("Querying {} purchased tickets from {} for user {} after {} and before {}", limit, offset, user_id);


        SqlParameterSource params = new MapSqlParameterSource().addValue("id", user_id)
                                                               .addValue("limit", limit)
                                                               .addValue("offset", offset)
                                                               .addValue("start_date", startDate)
                                                               .addValue("end_date", endDate);
        return namedTemplate.query(FIND_ALL_BY_USER, params, new HistoryTicketMapper());
    }
}
