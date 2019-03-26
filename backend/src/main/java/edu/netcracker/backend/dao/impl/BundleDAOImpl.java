package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.BundleDAO;
import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.dao.mapper.BundleRowMapper;
import edu.netcracker.backend.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@PropertySource("classpath:sql/bundledao.properties")
public class BundleDAOImpl extends CrudDAOImpl<Bundle> implements BundleDAO {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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
    @Value("${SELECT_BUNDLE_TRIP}")
    private String SELECT_BUNDLE_TRIP;
    @Value("${SELECT_TRIP_CLASS}")
    private String SELECT_TRIP_CLASS;
    @Value("${SELECT_CLASS_SERVICE}")
    private String SELECT_CLASS_SERVICE;
    @Value("${SELECT_BUNDLE_SERVICES}")
    private String SELECT_BUNDLE_SERVICES;
    @Value("${INSERT_BUNDLE_CLASS}")
    private String INSERT_BUNDLE_CLASS;
    @Value("${INSERT_BUNDLE_SERVICE}")
    private String INSERT_BUNDLE_SERVICE;
    @Value("${DELETE_BUNDLE_CLASSES_BY_ID}")
    private String DELETE_BUNDLE_CLASSES_BY_ID;
    @Value("${DELETE_BUNDLE_SERVICES_BY_ID}")
    private String DELETE_BUNDLE_SERVICES_BY_ID;

    private final TicketClassDAO ticketClassDAO;
    private final Logger logger = LoggerFactory.getLogger(BundleDAOImpl.class);

    @Autowired
    public BundleDAOImpl(TicketClassDAO ticketClassDAO, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.ticketClassDAO = ticketClassDAO;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Bundle> findAll() {
        logger.debug("BundleDAO.findAll was invoked");
        return getJdbcTemplate().query(GET_FRESH_BUNDLES, getGenericMapper());
    }

    @Override
    public List<Bundle> findAll(Number limit, Number offset) {
        logger.debug("Querying {} bundles from {}", limit, offset);
        List<Bundle> bundles = getJdbcTemplate().query(SELECT_BUNDLES,
                                                       new Object[]{limit, offset},
                                                       new BundleRowMapper());
        logger.debug("Setting bundle dependencies to bundles");
        attachBundleDependencies(bundles);
        return bundles;
    }

    @Override
    public Optional<Bundle> find(Number id) throws EmptyResultDataAccessException {
        logger.debug("Searching for bundle with id: {}", id);
        Optional<Bundle> optBundle = Optional.ofNullable(getJdbcTemplate().queryForObject(SELECT_BY_ID,
                                                                                          new Object[]{id},
                                                                                          new BundleRowMapper()));
        logger.debug("Setting bundle dependencies to bundles");
        List<Bundle> bundles = new ArrayList<>();
        optBundle.ifPresent(bundle -> bundles.add(bundle));
        optBundle.ifPresent(bundle -> attachBundleDependencies(bundles));
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
        logger.debug("Bundle services deleted: {}", services);
        int classes = getJdbcTemplate().update(DELETE_BUNDLE_CLASSES_BY_ID, id);
        logger.debug("Bundle classes deleted: {}", classes);
        getJdbcTemplate().update(DELETE_BUNDLE, id);
    }

    @Override
    public Long count() {
        return getJdbcTemplate().queryForObject(COUNT_BUNDLES, Long.class);
    }

    private void attachBundleDependencies(List<Bundle> bundles) {
        List<Trip> trips = attachTrips(bundles);
        List<TicketClass> classes = attachTicketClasses(trips);
        attachServices(classes);
    }

    private List<Trip> attachTrips(List<Bundle> bundles) {
        Map<Long, List<Trip>> relatedTrips = findAllTripsForBundles(bundles.stream()
                                                                           .map(Bundle::getBundleId)
                                                                           .collect(Collectors.toList()));
        List<Trip> trips = new ArrayList<>();
        for (Bundle bundle : bundles) {
            bundle.setBundleTrips(relatedTrips.get(bundle.getBundleId()));
            trips.addAll(relatedTrips.get(bundle.getBundleId()));
        }
        return trips;
    }

    private List<TicketClass> attachTicketClasses(List<Trip> trips) {
        Map<Long, List<TicketClass>> relatedTicketClasses = findAllTicketClassesForTrips(trips.stream()
                                                                                              .map(Trip::getTripId)
                                                                                              .collect(Collectors.toList()));
        List<TicketClass> ticketClasses = new ArrayList<>();
        for (Trip trip : trips) {
            trip.setTicketClasses(relatedTicketClasses.get(trip.getTripId()));
            ticketClasses.addAll(relatedTicketClasses.get(trip.getTripId()));
        }
        return ticketClasses;
    }

    private List<Service> attachServices(List<TicketClass> ticketClasses) {
        Map<Long, List<Service>> relatedServices = findAllServicesForClass(ticketClasses.stream()
                                                                                        .map(TicketClass::getClassId)
                                                                                        .collect(Collectors.toList()));
        List<Service> services = new ArrayList<>();
        for (TicketClass ticketClass : ticketClasses) {
            ticketClass.setServices(relatedServices.get(ticketClass.getClassId()));
            services.addAll(relatedServices.get(ticketClass.getClassId()));
        }
        return services;
    }

    private Map<Long, List<Trip>> findAllTripsForBundles(List<Long> bundleIds) {
        Map<Long, List<Trip>> relatedTrips = new HashMap<>();
        List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(SELECT_BUNDLE_TRIP,
                                                                                 new MapSqlParameterSource("bundleIds",
                                                                                                           bundleIds));
        for (Map<String, Object> row : rows) {
            List<Trip> trips = relatedTrips.computeIfAbsent((Long) row.get("bundle_id"), id -> new ArrayList<>());
            trips.add(createTrip(row));
        }
        return relatedTrips;
    }

    private Trip createTrip(Map<String, Object> row) {
        Trip t = new Trip();

        t.setTripId((Long) row.get("trip_id"));

        t.setDepartureSpaceport(new Spaceport());
        t.getDepartureSpaceport()
         .setSpaceportName((String) row.get("departure_spaceport"));
        t.getDepartureSpaceport()
         .setPlanet(new Planet());
        t.getDepartureSpaceport()
         .getPlanet()
         .setPlanetName((String) row.get("departure_planet"));

        t.setArrivalSpaceport(new Spaceport());
        t.getArrivalSpaceport()
         .setSpaceportName((String) row.get("arrival_spaceport"));
        t.getArrivalSpaceport()
         .setPlanet(new Planet());
        t.getArrivalSpaceport()
         .getPlanet()
         .setPlanetName((String) row.get("arrival_planet"));

        return t;
    }

    private Map<Long, List<TicketClass>> findAllTicketClassesForTrips(List<Long> tripIds) {
        Map<Long, List<TicketClass>> relatedTicketClasses = new HashMap<>();
        List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(SELECT_TRIP_CLASS,
                                                                                 new MapSqlParameterSource("tripIds",
                                                                                                           tripIds));
        for (Map<String, Object> row : rows) {
            List<TicketClass> ticketClasses = relatedTicketClasses.computeIfAbsent((Long) row.get("trip_id"),
                                                                                   id -> new ArrayList<>());
            ticketClasses.add(createTicketClass(row));
        }
        return relatedTicketClasses;
    }

    private TicketClass createTicketClass(Map<String, Object> row) {
        TicketClass t = new TicketClass();

        t.setTicketPrice((Integer) row.get("class_id"));

        t.setItemNumber((Integer) row.get("item_number"));

        return t;
    }

    private Map<Long, List<Service>> findAllServicesForClass(List<Long> classIds) {
        Map<Long, List<Service>> relatedServices = new HashMap<>();
        List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(SELECT_CLASS_SERVICE,
                                                                                 new MapSqlParameterSource("classIds",
                                                                                                           classIds));
        for (Map<String, Object> row : rows) {
            List<Service> services = relatedServices.computeIfAbsent((Long) row.get("class_id"),
                                                                     id -> new ArrayList<>());
            services.add(createService(row));
        }
        return relatedServices;
    }

    private Service createService(Map<String, Object> row) {
        Service s = new Service();

        s.setServiceId(((Integer) row.get("service_id")));

        s.setServiceName((String) row.get("service_name"));

        s.setServicePrice((Integer) row.get("service_price"));

        s.setItemNumber((Integer) row.get("item_number"));

        return s;
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
                parameters.add(new Object[]{bundleId,
                                            itemNumber,
                                            classId,
                                            service.getServiceId()});
            }
        }
        getJdbcTemplate().batchUpdate(INSERT_BUNDLE_SERVICE, parameters);
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
