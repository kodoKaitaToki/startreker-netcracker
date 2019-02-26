package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.BundleDAO;
import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.model.Bundle;
import edu.netcracker.backend.model.Service;
import edu.netcracker.backend.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class BundleDAOImpl extends CrudDAO<Bundle> implements BundleDAO {

    private static final String ORDER_BY = "ORDER BY bundle_id ";

    private static final String SELECT_ALL_BUNDLES = "SELECT bundle_id, " +
            "start_date, " +
            "finish_date, " +
            "bundle_price, " +
            "bundle_description, " +
            "bundle_photo " +
            "FROM bundle " +
            ORDER_BY;

    private static final String PAGING_SELECT_BUNDLES = SELECT_ALL_BUNDLES + "LIMIT ? OFFSET ?;";

    private static final String SELECT_BY_ID = SELECT_ALL_BUNDLES + "WHERE bundle_id = ?" + ORDER_BY;

    private static final String INSERT_BUNDLE = "INSERT INTO bundle ( " +
            "start_date, " +
            "finish_date, " +
            "bundle_price, " +
            "bundle_description, " +
            "bundle_photo " +
            " ) VALUES ( ?, ?, ?, ?, ?);";

    private static final String UPDATE_BUNDLE = "UPDATE bundle " +
            "SET start_date   = ?, " +
            "    finish_date  = ?, " +
            "    bundle_price = ?, " +
            "    bundle_description = ?, " +
            "    bundle_photo = ? " +
            "WHERE bundle_id = ?;";

    private static final String DELETE_BUNDLE = "DELETE FROM bundle WHERE bundle_id = ?;";

    private static final String COUNT_BUNDLES = "SELECT count(*) FROM bundle";

    private static final String SELECT_BUNDLE_TRIP = "SELECT DISTINCT " +
            "t.trip_id, " +
            "vehicle_id, " +
            "trip_status, " +
            "departure_date, " +
            "arrival_date, " +
            "creation_date " +
            "FROM trip t " +
            "INNER JOIN ticket_class tc ON t.trip_id = tc.trip_id " +
            "INNER JOIN bundle_class bc on tc.class_id = bc.class_id " +
            "INNER JOIN bundle b on bc.bundle_id = b.bundle_id " +
            "WHERE b.bundle_id = ?;";

    private static final String SELECT_BUNDLE_SERVICES = "SELECT " +
            "s.service_id, " +
            "service_name, " +
            "service_description, " +
            "service_price " +
            "FROM service s " +
            "INNER JOIN possible_service ps on s.service_id = ps.service_id " +
            "INNER JOIN bundle_service bs on ps.p_service_id = bs.p_service_id " +
            "INNER JOIN bundle b on bs.bundle_id = b.bundle_id " +
            "WHERE b.bundle_id = ? " +
            "ORDER BY s.service_id;";

    private static final String INSERT_BUNDLE_CLASS = "INSERT INTO bundle_class (bundle_id, class_id) " +
            "VALUES (?, ?);";

    private static final String INSERT_BUNDLE_SERVICE = "INSERT INTO bundle_service (bundle_id, p_service_id) " +
            "VALUES (?, (SELECT p_service_id FROM possible_service ps WHERE class_id = ? " +
            "                                                           AND service_id = ?));";

    private static final String SELECT_BUNDLE_CLASSES_BY_ID = "";

    private static final String SELECT_BUNDLE_SERVICES_BY_ID = "";

    private static final String DELETE_BUNDLE_CLASSES_BY_ID = "DELETE FROM bundle_class WHERE bundle_id = ?;";

    private static final String DELETE_BUNDLE_SERVICES_BY_ID = "DELETE FROM bundle_service WHERE bundle_id = ?;";

    private TripDAO tripDAO;
    private TicketClassDAO ticketClassDAO;

    @Autowired
    public BundleDAOImpl(TicketClassDAO ticketClassDAO, TripDAO tripDAO) {
        this.ticketClassDAO = ticketClassDAO;
        this.tripDAO = tripDAO;
    }

    @Override
    public List<Bundle> findAll() {
        logger.info("Querying all bundles");
        List<Bundle> bundles = getJdbcTemplate().query(SELECT_ALL_BUNDLES, getGenericMapper());
        logger.info("Setting bundle trip to bundles");
        bundles.forEach(bundle -> bundle.setBundleTrip(attachBundleTrip(bundle.getBundleId())));
        logger.info("Attaching bundles services");
        bundles.forEach(bundle -> bundle.setBundleServices(attachBundleServices(bundle.getBundleId())));
        return bundles;
    }

    @Override
    public List<Bundle> findAll(Number limit, Number offset) {
        logger.info(String.format("Querying %s bundles from %s", limit, offset));
        List<Bundle> bundles = getJdbcTemplate()
                .query(PAGING_SELECT_BUNDLES,
                        new Object[]{limit, offset},
                        getGenericMapper()
                );
        logger.info("Setting bundle trip to bundles");
        bundles.forEach(bundle -> bundle.setBundleTrip(attachBundleTrip(bundle.getBundleId())));
        logger.info("Attaching bundles services");
        bundles.forEach(bundle -> bundle.setBundleServices(attachBundleServices(bundle.getBundleId())));
        return bundles;
    }

    @Override
    public Optional<Bundle> find(Number id) {
        logger.info(String.format("Searching for bundle with id: %s", id));
        Optional<Bundle> optBundle = super.find(id);
        logger.info("Setting bundle trip to bundle");
        optBundle.ifPresent(bundle -> bundle.setBundleTrip(attachBundleTrip(bundle.getBundleId())));
        logger.info("Attaching bundle services");
        optBundle.ifPresent(bundle -> bundle.setBundleServices(attachBundleServices(bundle.getBundleId())));
        return optBundle;
    }

    @Override
    public void save(Bundle bundle) {
        super.save(bundle);
        saveBundleTrip(bundle);
        saveBundleServices(bundle);
    }

    @Override
    public void update(Bundle bundle) {
        super.update(bundle);
        updateBundleTrip(bundle);
        updateBundleServices(bundle);
    }

    @Override
    public void delete(Number id) {
        int services = getJdbcTemplate().update(DELETE_BUNDLE_SERVICES_BY_ID, id);
        logger.info("Bundle services deleted: %d", services);
        int classes = getJdbcTemplate().update(DELETE_BUNDLE_CLASSES_BY_ID, id);
        logger.info("Bundle classes deleted: %d", classes);
        getJdbcTemplate().update(DELETE_BUNDLE, id);
    }

    @Override
    public Long count() {
        return getJdbcTemplate().queryForObject(COUNT_BUNDLES, Long.class);
    }

    private Trip attachBundleTrip(Long bundleId) {
        Trip trip = getJdbcTemplate().queryForObject(SELECT_BUNDLE_TRIP, new Object[]{bundleId}, (resultSet, i) -> {
            Trip trip1 = new Trip();
            trip1.setTripId(resultSet.getLong(1));
            trip1.setVehicleId(resultSet.getLong(2));
            trip1.setTripStatus(resultSet.getInt(3));
            trip1.setDepartureDate(new Timestamp(resultSet.getDate(4).getTime()).toLocalDateTime());
            trip1.setArrivalDate(new Timestamp(resultSet.getDate(5).getTime()).toLocalDateTime());
            trip1.setCreationDate(resultSet.getDate(6).toLocalDate());
            return trip1;
        });
        if (trip != null)
            trip.setTicketClasses(ticketClassDAO.findByTripId(trip.getTripId()));
        return trip;
    }

    private List<Service> attachBundleServices(Long bundleId) {
        List<Service> services = new ArrayList<>();

        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(SELECT_BUNDLE_SERVICES, bundleId);
        for (Map<String, Object> row : rows) {
            Service service = new Service();
            service.setServiceId((Integer) row.get("service_id"));
            service.setServiceName((String) row.get("service_name"));
            service.setServiceDescription((String) row.get("service_description"));
            service.setServicePrice((Integer) row.get("service_price"));
            services.add(service);
        }
        return services;
    }

    private void saveBundleTrip(Bundle bundle) {
        bundle.getBundleTrip().getTicketClasses()
                .forEach(
                        ticketClass -> getJdbcTemplate()
                                .update(INSERT_BUNDLE_CLASS, bundle.getBundleId(), ticketClass.getClassId())
                );
    }

    private void saveBundleServices(Bundle bundle) {
        bundle.getBundleTrip().getTicketClasses()
                .forEach((ticketClass -> bundle.getBundleServices()
                        .forEach(service -> getJdbcTemplate().update(INSERT_BUNDLE_SERVICE, ticketClass.getClassId(), service.getServiceId()))));
    }

    private void updateBundleTrip(Bundle bundle) {
        //Not the best but quick and easy solution. Better to improve
        getJdbcTemplate().update(DELETE_BUNDLE_CLASSES_BY_ID, bundle.getBundleId());
        saveBundleServices(bundle);
    }

    private void updateBundleServices(Bundle bundle) {
        //Not the best but quick and easy solution. Better to improve
        getJdbcTemplate().update(DELETE_BUNDLE_SERVICES_BY_ID, bundle.getBundleId());
        saveBundleServices(bundle);
    }
}
