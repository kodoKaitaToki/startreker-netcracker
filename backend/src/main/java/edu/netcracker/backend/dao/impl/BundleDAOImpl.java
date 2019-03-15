package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.BundleDAO;
import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.dao.mapper.BundleRowMapper;
import edu.netcracker.backend.dao.mapper.BundleTripRowMapper;
import edu.netcracker.backend.dao.sql.BundleQueries;
import edu.netcracker.backend.model.Bundle;
import edu.netcracker.backend.model.Service;
import edu.netcracker.backend.model.Trip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class BundleDAOImpl extends CrudDAOImpl<Bundle> implements BundleDAO {


    private final TicketClassDAO ticketClassDAO;
    private final BundleTripRowMapper tripMapper;
    private final Logger logger = LoggerFactory.getLogger(BundleDAOImpl.class);

    @Autowired
    public BundleDAOImpl(TicketClassDAO ticketClassDAO, BundleTripRowMapper tripMapper) {
        this.ticketClassDAO = ticketClassDAO;
        this.tripMapper = tripMapper;
    }

    @Override
    public List<Bundle> findAll(Number limit, Number offset) {
        logger.debug("Querying {} bundles from {}", limit, offset);
        List<Bundle> bundles = getJdbcTemplate().query(BundleQueries.PAGING_SELECT_BUNDLES.toString(),
                                                       new Object[]{limit, offset}, new BundleRowMapper());
        logger.debug("Setting bundle trip to bundles");
        bundles.forEach(bundle -> bundle.setBundleTrips(attachBundleTrips(bundle)));
        return bundles;
    }

    @Override
    public Optional<Bundle> find(Number id) throws EmptyResultDataAccessException {
        logger.debug("Searching for bundle with id: {}", id);
        Optional<Bundle> optBundle
                = Optional.ofNullable(getJdbcTemplate().queryForObject(BundleQueries.SELECT_BY_ID.toString(),
                                                                       new Object[]{id},
                                                                       new BundleRowMapper()));
        logger.debug("Setting bundle trip to bundle");
        optBundle.ifPresent(bundle -> bundle.setBundleTrips(attachBundleTrips(bundle)));
        logger.debug("Attaching bundle services");

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
        int services = getJdbcTemplate().update(BundleQueries.DELETE_BUNDLE_SERVICES_BY_ID.toString(), id);
        logger.debug("Bundle services deleted: {}", services);
        int classes = getJdbcTemplate().update(BundleQueries.DELETE_BUNDLE_CLASSES_BY_ID.toString(), id);
        logger.debug("Bundle classes deleted: {}", classes);
        getJdbcTemplate().update(BundleQueries.DELETE_BUNDLE.toString(), id);
    }

    @Override
    public Long count() {
        return getJdbcTemplate().queryForObject(BundleQueries.COUNT_BUNDLES.toString(), Long.class);
    }

    private List<Trip> attachBundleTrips(Bundle bundle) {
        List<Trip> trips = getJdbcTemplate().query(BundleQueries.SELECT_BUNDLE_TRIP.toString(),
                                                   new Object[]{bundle.getBundleId()},
                                                   tripMapper);
        for (Trip trip : trips) {
            trip.setTicketClasses(ticketClassDAO.findTicketClassWithItemNumber(bundle.getBundleId(), trip.getTripId()));
            trip.getTicketClasses()
                .forEach(ticketClass -> ticketClass.setServices(attachBundleServices(bundle.getBundleId(),
                                                                                     ticketClass.getClassId())));
        }


        return trips;
    }

    private List<Service> attachBundleServices(Long bundleId, Long ticketClassId) {
        List<Service> services = new ArrayList<>();

        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(BundleQueries.SELECT_BUNDLE_SERVICES.toString(),
                                                                        bundleId,
                                                                        ticketClassId);
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
        bundle.getBundleTrips()
              .forEach(trip -> trip.getTicketClasses()
                                   .forEach(ticketClass -> getJdbcTemplate().update(BundleQueries.INSERT_BUNDLE_CLASS.toString(),
                                                                                    bundle.getBundleId(),
                                                                                    ticketClass.getClassId(),
                                                                                    ticketClass.getItemNumber())));
    }

    private void saveBundleServices(Bundle bundle) {
        bundle.getBundleTrips()
              .forEach(trip -> trip.getTicketClasses()
                                   .forEach(ticketClass -> ticketClass.getServices()
                                                                      .forEach(service -> getJdbcTemplate().update(
                                                                              BundleQueries.INSERT_BUNDLE_SERVICE.toString(),
                                                                              bundle.getBundleId(),
                                                                              ticketClass.getItemNumber(),
                                                                              ticketClass.getClassId(),
                                                                              service.getServiceId()))));
    }

    private void updateBundleTrip(Bundle bundle) {
        //Not the best but quick and easy solution.
        getJdbcTemplate().update(BundleQueries.DELETE_BUNDLE_CLASSES_BY_ID.toString(), bundle.getBundleId());
        saveBundleServices(bundle);
    }

    private void updateBundleServices(Bundle bundle) {
        //Not the best but quick and easy solution.
        getJdbcTemplate().update(BundleQueries.DELETE_BUNDLE_SERVICES_BY_ID.toString(), bundle.getBundleId());
        saveBundleServices(bundle);
    }


}
