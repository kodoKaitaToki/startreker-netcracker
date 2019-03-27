package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@PropertySource("classpath:sql/bundledao.properties")
@Component
public class BundleDAOAttacher {

    @Value("${SELECT_BUNDLE_TRIPS_AND_CLASSES}")
    private String SELECT_BUNDLE_TRIPS_AND_CLASSES;
    @Value("${SELECT_TRIP_INFO}")
    private String SELECT_TRIP_INFO;
    @Value("${SELECT_BUNDLE_SERVICES}")
    private String SELECT_BUNDLE_SERVICES;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public BundleDAOAttacher(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    void attachBundleDependencies(List<Bundle> bundles) {
        List<Trip> trips = attachTripsAndClasses(bundles);
        attachTripsInfo(trips);
        attachServices(bundles);
    }

    private List<Trip> attachTripsAndClasses(List<Bundle> bundles) {
        log.debug("Setting trips and classes to bundles");
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
        log.debug("Setting trip info");
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
        log.debug("Setting services to bundles");
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
}
