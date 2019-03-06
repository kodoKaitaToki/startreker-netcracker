package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.BundleDAO;
import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.dao.mapper.BundleRowMapper;
import edu.netcracker.backend.dao.mapper.TripRowMapper;
import edu.netcracker.backend.model.Bundle;
import edu.netcracker.backend.model.Service;
import edu.netcracker.backend.model.Trip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class BundleDAOImpl extends CrudDAOImpl<Bundle> implements BundleDAO {

    private static final String ORDER_BY = " ORDER BY bundle_id ";

    private static final String SELECT_ALL_BUNDLES = "SELECT bundle_id, " +
            "start_date, " +
            "finish_date, " +
            "bundle_price, " +
            "bundle_description, " +
            "bundle_photo " +
            "FROM bundle ";

    private static final String PAGING_SELECT_BUNDLES = SELECT_ALL_BUNDLES + ORDER_BY + "LIMIT ? OFFSET ?";

    private static final String SELECT_BY_ID = SELECT_ALL_BUNDLES + " WHERE bundle_id = ?";

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
            "trip_status, " +
            "departure_date, " +
            "arrival_date, " +
            "trip_photo, " +
            "creation_date, " +
            "bc.item_number " +
            "FROM trip t " +
            "INNER JOIN ticket_class tc ON t.trip_id = tc.trip_id " +
            "INNER JOIN bundle_class bc on tc.class_id = bc.class_id " +
            "INNER JOIN bundle b on bc.bundle_id = b.bundle_id " +
            "WHERE b.bundle_id = ?;";

    private static final String SELECT_BUNDLE_SERVICES = "SELECT " +
            "s.service_id, " +
            "service_name, " +
            "service_description, " +
            "service_price, " +
            "bs.item_number " +
            "FROM service s " +
            "INNER JOIN possible_service ps on s.service_id = ps.service_id " +
            "INNER JOIN bundle_service bs on ps.p_service_id = bs.p_service_id " +
            "INNER JOIN bundle b on bs.bundle_id = b.bundle_id " +
            "WHERE b.bundle_id = ? " +
            "ORDER BY s.service_id;";

    private static final String INSERT_BUNDLE_CLASS = "INSERT INTO bundle_class (bundle_id, class_id, item_number) " +
            "VALUES (?, ?, ?);";

    private static final String INSERT_BUNDLE_SERVICE = "INSERT INTO bundle_service (bundle_id, item_number, p_service_id) " +
            "VALUES (?, ?, (SELECT p_service_id FROM possible_service ps WHERE class_id = ? " +
            "                                                           AND service_id = ?));";

    private static final String DELETE_BUNDLE_CLASSES_BY_ID = "DELETE FROM bundle_class WHERE bundle_id = ?;";

    private static final String DELETE_BUNDLE_SERVICES_BY_ID = "DELETE FROM bundle_service WHERE bundle_id = ?;";

    private TripDAO tripDAO;
    private TicketClassDAO ticketClassDAO;
    private final Logger logger = LoggerFactory.getLogger(BundleDAOImpl.class);

    @Autowired
    public BundleDAOImpl(TicketClassDAO ticketClassDAO, TripDAO tripDAO) {
        this.ticketClassDAO = ticketClassDAO;
        this.tripDAO = tripDAO;
    }

    @Override
    public List<Bundle> findAll(Number limit, Number offset) {
        logger.info(String.format("Querying %s bundles from %s", limit, offset));
        List<Bundle> bundles = getJdbcTemplate()
                .query(PAGING_SELECT_BUNDLES,
                        new Object[]{limit, offset},
                        new BundleRowMapper());
        logger.info("Setting bundle trip to bundles");
        bundles.forEach(bundle -> bundle.setBundleTrips(attachBundleTrips(bundle.getBundleId())));
        logger.info("Attaching bundles services");
        bundles.forEach(bundle -> bundle.setBundleServices(attachBundleServices(bundle.getBundleId())));
        return bundles;
    }

    @Override
    public Optional<Bundle> find(Number id) {
        logger.info(String.format("Searching for bundle with id: %s", id));
        Optional<Bundle> optBundle = Optional.ofNullable(getJdbcTemplate().queryForObject(SELECT_BY_ID, new Object[]{id}, new BundleRowMapper()));

        logger.info("Setting bundle trip to bundle");
        optBundle.ifPresent(bundle -> bundle.setBundleTrips(attachBundleTrips(bundle.getBundleId())));
        logger.info("Attaching bundle services");
        optBundle.ifPresent(bundle -> bundle.setBundleServices(attachBundleServices(bundle.getBundleId())));
        return optBundle;
    }

    @Override
    public void save(Bundle bundle) {
        super.save(bundle);
        saveBundleTrips(bundle);
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

    private List<Trip> attachBundleTrips(Long bundleId) {
        List<Trip> trips = getJdbcTemplate().query(SELECT_BUNDLE_TRIP, new Object[]{bundleId}, new TripRowMapper());

        trips.forEach(trip -> trip.setTicketClasses(ticketClassDAO.findTicketClassWithItemNumber(bundleId, trip.getTripId())));
        return trips;
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
            service.setItemNumber((Integer) row.get("item_number"));
            services.add(service);
        }
        return services;
    }

    private void saveBundleTrips(Bundle bundle) {
        bundle.getBundleTrips().forEach(trip -> trip.getTicketClasses()
                .forEach(ticketClass -> getJdbcTemplate()
                        .update(INSERT_BUNDLE_CLASS,
                                bundle.getBundleId(),
                                ticketClass.getClassId(),
                                ticketClass.getItemNumber()
                        )
                )
        );
    }

    private void saveBundleServices(Bundle bundle) {
        bundle.getBundleTrips()
                .forEach(trip -> trip.getTicketClasses()
                        .forEach(ticketClass -> bundle.getBundleServices()
                                .forEach(service -> getJdbcTemplate()
                                        .update(INSERT_BUNDLE_SERVICE,
                                                bundle.getBundleId(),
                                                ticketClass.getItemNumber(),
                                                ticketClass.getClassId(),
                                                service.getServiceId())
                                )
                        )
                );
    }

    private void updateBundleTrip(Bundle bundle) {
        //Not the best but quick and easy solution.
        getJdbcTemplate().update(DELETE_BUNDLE_CLASSES_BY_ID, bundle.getBundleId());
        saveBundleServices(bundle);
    }

    private void updateBundleServices(Bundle bundle) {
        //Not the best but quick and easy solution.
        getJdbcTemplate().update(DELETE_BUNDLE_SERVICES_BY_ID, bundle.getBundleId());
        saveBundleServices(bundle);
    }


}
