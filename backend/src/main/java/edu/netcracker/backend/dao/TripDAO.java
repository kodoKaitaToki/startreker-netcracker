package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.TripWithArrivalAndDepartureData;

import java.util.List;
import java.util.Optional;

public interface TripDAO {

    void save(Trip trip);

    Optional<Trip> find(Number id);

    void delete(Trip trip);

    List<Trip> findByCarrierId(Number id);

    List<Trip> findAll();

    List<TripWithArrivalAndDepartureData> getAllTripsWitArrivalAndDepatureDataBelongToCarrier(Number carrierId);

    List<Trip> findAllByCarrierAndStatus(Integer userId, Integer status, Long offset, Long limit);

    List<Trip> findAllByCarrier(Integer userId, Integer ignoredStatus, Long offset, Long limit);

    List<Trip> findAllByApproverByStatus(Integer userId, Integer status, Long offset, Long limit);

    List<Trip> findAllByStatus(Integer status, Long offset, Long limit);
}
