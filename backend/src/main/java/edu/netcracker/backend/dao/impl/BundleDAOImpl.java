package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.BundleDAO;
import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.model.Bundle;
import edu.netcracker.backend.model.Service;
import edu.netcracker.backend.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

    private static final String DELETE_BUNDLE = "DELETE FROM bundle " +
            "WHERE bundle_id = ?;";

    private static final String COUNT_BUNDLES = "SELECT count(*) FROM bundle";

    private static final String SELECT_BUNDLE_TRIP = "SELECT " +
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

    private TicketClassDAO ticketClassDAO;

    @Autowired
    public BundleDAOImpl(TicketClassDAO ticketClassDAO) {
        this.ticketClassDAO = ticketClassDAO;
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
        List<Bundle> bundles = getJdbcTemplate().query(PAGING_SELECT_BUNDLES, getGenericMapper());
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
    public void update(Bundle bundle) {
        super.update(bundle);
    }

    @Override
    public void delete(Number id) {

    }

    @Override
    public Long count() {
        return getJdbcTemplate().queryForObject(COUNT_BUNDLES, Long.class);
    }

    private Trip attachBundleTrip(Long bundleId) {
        Trip trip = getJdbcTemplate().queryForObject(SELECT_BUNDLE_TRIP, new Object[]{bundleId}, Trip.class);
        if (trip != null)
            trip.setTicketClasses(ticketClassDAO.findByTripId(trip.getTripId()));
        return trip;
    }

    private List<Service> attachBundleServices(Long bundleId) {
        List<Service> services = new ArrayList<>();

        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(SELECT_BUNDLE_SERVICES, bundleId);
        for (Map<String, Object> row : rows) {
            Service service = new Service();
            service.setServiceId((Long) row.get("service_id"));
            service.setServiceName((String) row.get("service_name"));
            service.setServiceDescription((String) row.get("service_description"));
            service.setServicePrice((Integer) row.get("service_price"));
            services.add(service);
        }
        return services;
    }
}



