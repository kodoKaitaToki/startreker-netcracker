package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.BundleDAO;
import edu.netcracker.backend.dao.mapper.BundleRowMapper;
import edu.netcracker.backend.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@PropertySource("classpath:sql/bundledao.properties")
public class BundleDAOImpl extends CrudDAOImpl<Bundle> implements BundleDAO {

    @Value("${GET_FRESH_BUNDLES}")
    private String GET_FRESH_BUNDLES;
    @Value("${SELECT_BUNDLES}")
    private String SELECT_BUNDLES;
    @Value("${SELECT_BY_ID}")
    private String SELECT_BY_ID;
    @Value("${DELETE_BUNDLE}")
    private String DELETE_BUNDLE;
    @Value("${COUNT_BUNDLES}")
    private String COUNT_BUNDLES;
    @Value("${INSERT_BUNDLE_CLASS}")
    private String INSERT_BUNDLE_CLASS;
    @Value("${INSERT_BUNDLE_SERVICE}")
    private String INSERT_BUNDLE_SERVICE;
    @Value("${DELETE_BUNDLE_CLASSES_BY_ID}")
    private String DELETE_BUNDLE_CLASSES_BY_ID;
    @Value("${DELETE_BUNDLE_SERVICES_BY_ID}")
    private String DELETE_BUNDLE_SERVICES_BY_ID;


    private BundleDAOAttacher bundleDAOAttacher;

    @Autowired
    public BundleDAOImpl(BundleDAOAttacher bundleDAOAttacher) {
        this.bundleDAOAttacher = bundleDAOAttacher;
    }

    @Override
    public List<Bundle> findAll() {
        log.debug("BundleDAO.findAll was invoked");
        return getJdbcTemplate().query(GET_FRESH_BUNDLES, getGenericMapper());
    }

    @Override
    public List<Bundle> findAll(Number limit, Number offset) {
        log.info("Querying {} bundles from {}", limit, offset);
        List<Bundle> bundles = getJdbcTemplate().query(SELECT_BUNDLES,
                                                       new Object[]{limit, offset},
                                                       new BundleRowMapper());
        log.debug("Got {} bundles", bundles.size());
        log.debug("Setting bundle dependencies to bundles");
        bundleDAOAttacher.attachBundleDependencies(bundles);
        return bundles;
    }

    @Override
    public Optional<Bundle> find(Number id) throws EmptyResultDataAccessException {
        log.debug("Searching for bundle with id: {}", id);
        Optional<Bundle> optBundle = Optional.ofNullable(getJdbcTemplate().queryForObject(SELECT_BY_ID,
                                                                                          new Object[]{id},
                                                                                          new BundleRowMapper()));
        log.debug("Setting bundle dependencies to bundle");
        List<Bundle> bundles = new ArrayList<>();
        optBundle.ifPresent(bundles::add);
        optBundle.ifPresent(bundle -> bundleDAOAttacher.attachBundleDependencies(bundles));
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
        log.debug("Deleting bundle {}", id);
        int services = getJdbcTemplate().update(DELETE_BUNDLE_SERVICES_BY_ID, id);
        log.debug("Bundle services deleted: {}", services);
        int classes = getJdbcTemplate().update(DELETE_BUNDLE_CLASSES_BY_ID, id);
        log.debug("Bundle classes deleted: {}", classes);
        getJdbcTemplate().update(DELETE_BUNDLE, id);
    }

    @Override
    public Long count() {
        return getJdbcTemplate().queryForObject(COUNT_BUNDLES, Long.class);
    }

    private void saveBundleTrips(Bundle bundle) {
        Long bundleId = bundle.getBundleId();
        List<TicketClass> ticketClasses = bundle.getBundleTrips()
                                                .stream()
                                                .map(Trip::getTicketClasses)
                                                .flatMap(Collection::stream)
                                                .collect(Collectors.toList());
        getJdbcTemplate().batchUpdate(INSERT_BUNDLE_CLASS,
                                      ticketClasses.stream()
                                                   .map(x -> new Object[]{bundleId, x.getClassId(), x.getItemNumber()})
                                                   .collect(Collectors.toList()));
        log.debug("Saved bundle {} trips", bundleId);
    }

    private void saveBundleServices(Bundle bundle) {
        Long bundleId = bundle.getBundleId();
        List<TicketClass> ticketClasses = bundle.getBundleTrips()
                                                .stream()
                                                .map(Trip::getTicketClasses)
                                                .flatMap(Collection::stream)
                                                .collect(Collectors.toList());
        List<Object[]> parameters = new ArrayList<>();
        for (TicketClass ticketClass : ticketClasses) {
            Integer itemNumber = ticketClass.getItemNumber();
            Long classId = ticketClass.getClassId();
            for (Service service : ticketClass.getServices()) {
                parameters.add(new Object[]{bundleId, itemNumber, classId, service.getServiceId()});
            }
        }
        getJdbcTemplate().batchUpdate(INSERT_BUNDLE_SERVICE, parameters);
        log.debug("Saved bundle {} services", bundleId);
    }

    private void updateBundleTrip(Bundle bundle) {
        //Not the best but quick and easy solution.
        getJdbcTemplate().update(DELETE_BUNDLE_CLASSES_BY_ID, bundle.getBundleId());
        saveBundleTrips(bundle);
    }

    private void updateBundleServices(Bundle bundle) {
        //Not the best but quick and easy solution.
        getJdbcTemplate().update(DELETE_BUNDLE_SERVICES_BY_ID, bundle.getBundleId());
        saveBundleServices(bundle);
    }


}
