package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.DiscountDAO;
import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.dao.TicketDAO;
import edu.netcracker.backend.dao.mapper.DiscountMapper;
import edu.netcracker.backend.dao.mapper.TicketClassWithDiscountMapper;
import edu.netcracker.backend.model.TicketClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@PropertySource("classpath:sql/ticketclassdao.properties")
@Slf4j
public class TicketClassDAOImpl extends CrudDAOImpl<TicketClass> implements TicketClassDAO {

    @Value("${SELECT_BY_TRIP_ID_WITH_ITEM_NUMBER}")
    private String SELECT_BY_TRIP_ID_WITH_ITEM_NUMBER;

    @Value("${SELECT_BY_TRIP_ID}")
    private String SELECT_BY_TRIP_ID;

    @Value("${SELECT_BY_TRIP_ID_WITH_DISCOUNTS}")
    private String SELECT_BY_TRIP_ID_WITH_DISCOUNTS;

    @Value("${GET_ALL_TICKET_CLASSES_RELATED_TO_CARRIER}")
    private String GET_ALL_TICKET_CLASSES_RELATED_TO_CARRIER;

    @Value("${GET_TICKET_CLASS_WITH_DISCOUNT}")
    private String GET_TICKET_CLASS_WITH_DISCOUNT;

    @Value("${GET_ALL_TICKET_CLASSES_BELONG_TO_TRIPS_BELONG_TO_CARRIER}")
    private String GET_ALL_TICKET_CLASSES_BELONG_TO_TRIPS_BELONG_TO_CARRIER;

    @Value("${GET_TICKET_CLASSES_BELONG_TO_CARRIER}")
    private String GET_TICKET_CLASSES_BELONG_TO_CARRIER;

    @Value("${INSERT_TICKET_CLASS}")
    private String INSERT_TICKET_CLASS;

    @Value("${UPDATE_TICKET_CLASS}")
    private String UPDATE_TICKET_CLASS;

    @Value("${FIND_ID_BY_CLASS_NAME_AND_TRIP_ID}")
    private String FIND_ID_BY_CLASS_NAME_AND_TRIP_ID;

    @Value("${DELETE_TICKET_CLASS_BY_ID}")
    private String DELETE_TICKET_CLASS_BY_ID;

    @Value("${GET_NUMBER_OF_TICKET_CLASSES_BY_NAME_AND_TRIP_ID}")
    private String GET_NUMBER_OF_TICKET_CLASSES_BY_NAME_AND_TRIP_ID;

    @Value("${GET_TICKET_CLASS_BY_NAME_AND_TRIP_ID}")
    private String GET_TICKET_CLASS_BY_NAME_AND_TRIP_ID;

    private DiscountMapper discountMapper;

    private final TicketDAO ticketDAO;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public TicketClassDAOImpl(DiscountMapper discountMapper,
                              NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                              TicketDAO ticketDAO) {
        this.discountMapper = discountMapper;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.ticketDAO = ticketDAO;
    }

    @Override
    public Optional<TicketClass> findTicketClassBelongToCarrier(Number ticketClassId, Number carrierId) {
        try {
            TicketClass ticketClass = getJdbcTemplate().queryForObject(GET_TICKET_CLASSES_BELONG_TO_CARRIER,
                                                                       new Object[]{carrierId, ticketClassId},
                                                                       getGenericMapper());
            return Optional.of(ticketClass);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TicketClass> findByTripId(Number id) {
        List<TicketClass> ticketClasses = new ArrayList<>();

        log.debug("Getting all ticket classes for trip with id {}", id);
        ticketClasses.addAll(getJdbcTemplate().query(SELECT_BY_TRIP_ID, new Object[]{id}, getGenericMapper()));

        log.debug("Counting number of remaining seats for each ticket class");
        ticketClasses.forEach(ticketClass -> ticketClass.setRemainingSeats(ticketDAO.getRemainingSeatsForClass(
                ticketClass.getClassId())));

        return ticketClasses;
    }

    @Override
    public List<TicketClass> findByTripIdWithDiscount(Number id) {
        List<TicketClass> ticketClasses = new ArrayList<>();

        log.debug("Getting all ticket classes with discounts for trip with id {}", id);
        ticketClasses.addAll(getJdbcTemplate().query(SELECT_BY_TRIP_ID_WITH_DISCOUNTS,
                                                     new Object[]{id},
                                                     new TicketClassWithDiscountMapper(discountMapper)));

        log.debug("Counting number of remaining seats for each ticket class");
        ticketClasses.forEach(ticketClass -> ticketClass.setRemainingSeats(ticketDAO.getRemainingSeatsForClass(
                ticketClass.getClassId())));

        return ticketClasses;
    }

    /**
     * Method for getting ticket class by its name and id of trip
     *
     * @param tripId - id of trip ticket class belongs to
     * @param name   - name of ticket class
     */
    @Override
    public TicketClass getTicketClassByNameAndTripId(Long tripId, String name) {
        log.debug("Getting ticket class by its with name {} and id of trip {}", name, tripId);
        return getJdbcTemplate().queryForObject(GET_TICKET_CLASS_BY_NAME_AND_TRIP_ID,
                                                new Object[]{tripId, name.toLowerCase()},
                                                getGenericMapper());
    }

    /**
     * Method for adding new ticket classes to database
     *
     * @param ticketClass - ticket class to be added
     */
    @Override
    public void create(TicketClass ticketClass) {
        log.debug("Adding new ticket class for trip with id {}", ticketClass.getTripId());
        getJdbcTemplate().update(INSERT_TICKET_CLASS,
                                 ticketClass.getClassName(),
                                 ticketClass.getTripId(),
                                 ticketClass.getClassSeats(),
                                 ticketClass.getTicketPrice());
    }

    /**
     * Method for updating of ticket classes
     *
     * @param ticketClass - ticket class to be updated
     */
    @Override
    public void update(TicketClass ticketClass) {
        log.debug("Updating ticket class with id {}", ticketClass.getClassId());
        super.update(ticketClass);
    }

    /**
     * Method for checking whether ticket class with specified name and trip id exists in database
     *
     * @param tripId    - trip_id of ticket class
     * @param className - name of ticket class
     */
    @Override
    public boolean exists(Long tripId, String className) {
        log.debug("Counting number of ticket classes with name {} for trip with id {}", className, tripId);
        Integer amount = getJdbcTemplate().queryForObject(GET_NUMBER_OF_TICKET_CLASSES_BY_NAME_AND_TRIP_ID,
                                                          new Object[]{tripId, className.toLowerCase()},
                                                          Integer.class);
        if (amount.equals(new Integer(0))) {
            log.debug("0 ticket classes found, ticket class doesn't exist");
            return false;
        }

        log.debug("Ticket class with name {} exists for trip with id {}", className, tripId);
        return true;
    }

    /**
     * Sophisticated method for selecting ticketClasses with item_number
     *
     * @param bundleId - id of bundle
     * @param tripId   - id of trip
     * @return list of ticket classes with item_number required for bundles
     */
    public List<TicketClass> findTicketClassWithItemNumber(Number bundleId, Number tripId) {
        log.debug(
                "TicketClassDAOImpl.findTicketClassWithItemNumber(Number bundleId, Number tripId) was invoked with parameters {}, {}",
                bundleId,
                tripId);
        return getJdbcTemplate().query(SELECT_BY_TRIP_ID_WITH_ITEM_NUMBER,
                                       new Object[]{bundleId, tripId},
                                       (resultSet, i) -> {
                                           TicketClass tc = new TicketClass();
                                           tc.setClassId(resultSet.getLong(1));
                                           tc.setClassName(resultSet.getString(2));
                                           tc.setTripId(resultSet.getLong(3));
                                           tc.setTicketPrice(resultSet.getInt(4));
                                           tc.setItemNumber(resultSet.getInt(5));
                                           return tc;
                                       });
    }

    @Override
    public List<TicketClass> getAllTicketClassesRelatedToCarrier(Number carrierId) {
        log.debug(
                "TicketClassDAOImpl.getAllTicketClassesRelatedToCarrier(Number carrierId) was invoked with parameter {}",
                carrierId);
        return new ArrayList<>(getJdbcTemplate().query(GET_ALL_TICKET_CLASSES_RELATED_TO_CARRIER,
                                                       new Object[]{carrierId},
                                                       getGenericMapper()));
    }

    public Optional<TicketClass> getTicketClassByDiscount(Number userId, Number discountId) {
        log.debug(
                "TicketClassDAOImpl.getTicketClassByDiscount(Number userId, Number discountId) was invoked with parameters {}, {}",
                userId,
                discountId);
        try {
            TicketClass ticketClass = getJdbcTemplate().queryForObject(GET_TICKET_CLASS_WITH_DISCOUNT,
                                                                       new Object[]{userId, discountId},
                                                                       getGenericMapper());
            return ticketClass != null ? Optional.of(ticketClass) : Optional.empty();
        } catch (EmptyResultDataAccessException e) {
            log.warn("No such ticketClass that has discount with id {} and belongs to user {}", discountId, userId);
            return Optional.empty();
        }
    }

    @Override
    public Map<Long, List<TicketClass>> getAllTicketClassesBelongToTrips(List<Number> tripIds) {
        log.debug(
                "TicketClassDAOImpl.getAllTicketClassesBelongToTrips(List<Number> tripIds) was invoked with parameters {}",
                tripIds);
        Map<Long, List<TicketClass>> relatedTicketClasses = new HashMap<>();

        List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(
                GET_ALL_TICKET_CLASSES_BELONG_TO_TRIPS_BELONG_TO_CARRIER,
                new MapSqlParameterSource("tripIds", tripIds));
        for (Map<String, Object> row : rows) {
            List<TicketClass> ticketClasses
                    = relatedTicketClasses.computeIfAbsent((((Number) row.get("trip_id")).longValue()),
                                                           aLong -> new ArrayList<>());

            ticketClasses.add(createTicketClass(row));
        }

        return relatedTicketClasses;
    }

    @Override
    public Long getTicketClassId(String className, Long tripId) {
        log.debug("Getting id of ticket class where trip id is {} and class name - {}", tripId, className);
        return getJdbcTemplate().queryForObject(FIND_ID_BY_CLASS_NAME_AND_TRIP_ID,
                                                new Object[]{className, tripId},
                                                Long.class);
    }

    @Override
    public void deleteTicketClassById(Long id) {
        log.debug("Deleting ticket class with id {}", id);
        getJdbcTemplate().update(DELETE_TICKET_CLASS_BY_ID, id);
    }

    private TicketClass createTicketClass(Map<String, Object> row) {
        return TicketClass.builder()
                          .classId(((Number) row.get("class_id")).longValue())
                          .className((String) row.get("class_name"))
                          .tripId(((Number) row.get("trip_id")).longValue())
                          .ticketPrice((Integer) row.get("ticket_price"))
                          .discountId(DiscountDAO.getDiscountId(row.get("discount_id")))
                          .classSeats((Integer) row.get("class_seats"))
                          .build();
    }
}
