package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.TripWithArrivalAndDepartureData;

import java.util.List;
import java.util.Optional;

public interface TripDAO {

    void save(Trip trip);

    Optional<Trip> find(Number id);

    List<Trip> allCarriersTrips(Long carrierId);

    List<Trip> paginationForCarrier(Integer limit, Integer offset, Long carrierId);

    List<Trip> findByStatusForCarrier(Integer status, Long carrierId);

    List<Trip> findByStatusForCarrierPagination(Integer status, Long carrierId, Integer limit, Integer offset);

    List<Trip> findByPlanetsForCarrier(String departurePlanet, String arrivalPlanet, Long carrierId);

    List<Trip> getAllTripsForUser(String departurePlanet,
                                  String departureSpaceport,
                                  String departureDate,
                                  String arrivalPlanet,
                                  String arrivalSpaceport,
                                  Integer limit,
                                  Integer offset);

    void add(Trip trip);

    void update(Trip trip);

    void delete(Trip trip);

    List<Trip> findByCarrierId(Number id);

    List<Trip> findAll();

    List<TripWithArrivalAndDepartureData> getAllTripsWitArrivalAndDepatureDataBelongToCarrier(Number carrierId);

    void updateTripInfo(Trip trip);

    List<Trip> findAllByCarrierAndStatus(Integer userId, Integer status, Long offset, Long limit);

    List<Trip> findAllByCarrier(Integer userId, Integer ignoredStatus, Long offset, Long limit);

    List<Trip> findAllByApproverByStatus(Integer userId, Integer status, Long offset, Long limit);

    List<Trip> findAllByStatus(Integer status, Long offset, Long limit);

    List<Trip> getAllTripsForUser(String departurePlanet,
                                  String departureSpaceport,
                                  String departureDate,
                                  String arrivalPlanet,
                                  String arrivalSpaceport,
                                  Integer limit,
                                  Integer offset);
}
