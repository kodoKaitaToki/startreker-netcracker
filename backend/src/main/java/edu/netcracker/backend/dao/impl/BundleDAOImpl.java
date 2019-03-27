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
    @Value("${SELECT_BUNDLE_TRIPS_AND_CLASSES}")
    private String SELECT_BUNDLE_TRIPS_AND_CLASSES;
    @Value("${SELECT_TRIP_INFO}")
    private String SELECT_TRIP_INFO;
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

    private final Logger logger = LoggerFactory.getLogger(BundleDAOImpl.class);

    @Autowired
    public BundleDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Bundle> findAll() {
        logger.debug("BundleDAO.findAll was invoked");
        return getJdbcTemplate().query(GET_FRESH_BUNDLES, getGenericMapper());
    }

    @Override
    public List<Bundle> findAll(Number limit, Number offset) {
        logger.info("Querying {} bundles from {}", limit, offset);
        List<Bundle> bundles = getJdbcTemplate().query(SELECT_BUNDLES,
                                                       new Object[]{limit, offset},
                                                       new BundleRowMapper());
        logger.info("Got {} bundles", bundles.size());
        logger.info("Setting bundle dependencies to bundles");
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
        optBundle.ifPresent(bundles::add);
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
        List<Trip> trips = attachTripsAndClasses(bundles);
        attachTripsInfo(trips);
        attachServices(bundles);
    }

    private List<Trip> attachTripsAndClasses(List<Bundle> bundles) {
        logger.info("Setting bundle trips to bundles");
        Map<Integer, List<Trip>> relatedTrips = findAllTripsAndClassesForBundles(bundles.stream()
                                                                                        .map(Bundle::getBundleId)
                                                                                        .collect(Collectors.toList()));
        List<Trip> trips = new ArrayList<>();
        for (Bundle bundle : bundles) {
            bundle.setBundleTrips(relatedTrips.get(bundle.getBundleId()
                                                         .intValue()));
            trips.addAll(relatedTrips.get(bundle.getBundleId()
                                                .intValue()));
        }
        return trips;
    }

    private Map<Integer, List<Trip>> findAllTripsAndClassesForBundles(List<Long> bundleIds) {
        Map<Integer, Map<Integer, List<TicketClass>>> relatedTicketClasses = new HashMap<>();
        List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(SELECT_BUNDLE_TRIPS_AND_CLASSES,
                                                                                 new MapSqlParameterSource("bundleIds",
                                                                                                           bundleIds));
        for (Map<String, Object> row : rows) {
            Map<Integer, List<TicketClass>> trips = relatedTicketClasses.computeIfAbsent((Integer) row.get("bundle_id"),
                                                                                         id -> new HashMap<>());
            List<TicketClass> ticketClasses = trips.computeIfAbsent((Integer) row.get("trip_id"),
                                                                    id -> new ArrayList<>());
            ticketClasses.add(createTicketClass(row));
        }
        Map<Integer, List<Trip>> relatedTrips = new HashMap<>();
        relatedTicketClasses.forEach((k, v) -> relatedTrips.put(k, convertTripsToList(v)));
        return relatedTrips;
    }

    private List<Trip> convertTripsToList(Map<Integer, List<TicketClass>> tripData) {
        List<Trip> trips = new ArrayList<>();
        tripData.forEach((k, v) -> trips.add(createTrip(k, v)));
        return trips;
    }

    private Trip createTrip(Integer tripId, List<TicketClass> ticketClasses) {
        Trip t = new Trip();
        t.setTripId(tripId.longValue());
        t.setTicketClasses(ticketClasses);
        return t;
    }

    private TicketClass createTicketClass(Map<String, Object> row) {
        TicketClass t = new TicketClass();

        t.setClassId(((Integer) row.get("class_id")).longValue());

        t.setItemNumber((Integer) row.get("item_number"));

        t.setTicketPrice((Integer) row.get("ticket_price"));

        t.setClassName((String) row.get("class_name"));

        return t;
    }

    private void attachTripsInfo(List<Trip> trips) {
        logger.info("Setting bundle trip info to bundles");
        findAllTripsInfo(trips.stream()
                              .collect(Collectors.groupingBy(x -> x.getTripId()
                                                                   .intValue(),
                                                             Collectors.toCollection(ArrayList::new))));
    }

    private void findAllTripsInfo(Map<Integer, List<Trip>> trips) {
        List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(SELECT_TRIP_INFO,
                                                                                 new MapSqlParameterSource("tripIds",
                                                                                                           new ArrayList<>(
                                                                                                                   trips.keySet())));
        for (Map<String, Object> row : rows) {
            List<Trip> tripsList = trips.computeIfAbsent((Integer) row.get("trip_id"), id -> new ArrayList<>());
            setTripInfo(tripsList, row);
        }
    }

    private void setTripInfo(List<Trip> trips, Map<String, Object> row) {
        for (Trip t : trips) {
            System.out.println(t.getTripId());
            t.setDepartureSpaceport(new Spaceport());
            t.getDepartureSpaceport()
             .setSpaceportName((String) row.get("departure_spaceport"));
            t.setDeparturePlanet(new Planet());
            t.getDeparturePlanet()
             .setPlanetName((String) row.get("departure_planet"));
            t.getDepartureSpaceport()
             .setPlanet(t.getDeparturePlanet());

            t.setArrivalSpaceport(new Spaceport());
            t.getArrivalSpaceport()
             .setSpaceportName((String) row.get("arrival_spaceport"));
            t.setArrivalPlanet(new Planet());
            t.getArrivalPlanet()
             .setPlanetName((String) row.get("arrival_planet"));
            t.getArrivalSpaceport()
             .setPlanet(t.getArrivalPlanet());
        }
    }

    private void attachServices(List<Bundle> bundles) {
        logger.info("Setting services to bundles");
        Map<Integer, Map<Integer, List<Service>>> relatedServicesToBundle = findAllServicesForBundles(bundles.stream()
                                                                                                             .map(Bundle::getBundleId)
                                                                                                             .collect(
                                                                                                                     Collectors.toList()));
        Map<Integer, List<Service>> relatedServicesToClass;
        List<TicketClass> ticketClasses;
        for (Bundle bundle : bundles) {
            relatedServicesToClass = relatedServicesToBundle.computeIfAbsent(bundle.getBundleId()
                                                                                   .intValue(), id -> new HashMap<>());
            ticketClasses = bundle.getBundleTrips()
                                  .stream()
                                  .map(Trip::getTicketClasses)
                                  .flatMap(Collection::stream)
                                  .collect(Collectors.toList());
            for (TicketClass ticketClass : ticketClasses) {
                ticketClass.setServices(relatedServicesToClass.computeIfAbsent(ticketClass.getClassId()
                                                                                          .intValue(),
                                                                               id -> new ArrayList<>()));
            }
        }
    }

    private Map<Integer, Map<Integer, List<Service>>> findAllServicesForBundles(List<Long> bundleIds) {
        Map<Integer, Map<Integer, List<Service>>> relatedServicesToBundle = new HashMap<>();
        List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(SELECT_BUNDLE_SERVICES,
                                                                                 new MapSqlParameterSource("bundleIds",
                                                                                                           bundleIds));
        for (Map<String, Object> row : rows) {
            Map<Integer, List<Service>> relatedServicesToClass
                    = relatedServicesToBundle.computeIfAbsent((Integer) row.get("bundle_id"), id -> new HashMap<>());
            List<Service> ticketClasses = relatedServicesToClass.computeIfAbsent((Integer) row.get("class_id"),
                                                                                 id -> new ArrayList<>());
            ticketClasses.add(createService(row));
        }
        return relatedServicesToBundle;
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
                parameters.add(new Object[]{bundleId, itemNumber, classId, service.getServiceId()});
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
