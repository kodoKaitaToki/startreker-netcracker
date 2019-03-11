package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.PlanetDAO;
import edu.netcracker.backend.dao.SpaceportDAO;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.dao.impl.BundleDAOImpl;
import edu.netcracker.backend.message.request.trips.TripCreation;
import edu.netcracker.backend.message.response.TripDTO;
import edu.netcracker.backend.message.response.trips.AllCarriersTripsDTO;
import edu.netcracker.backend.model.Planet;
import edu.netcracker.backend.model.Spaceport;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.model.state.trip.Draft;
import edu.netcracker.backend.model.state.trip.TripState;
import edu.netcracker.backend.model.state.trip.TripStateRegistry;
import edu.netcracker.backend.service.TripService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {

    private TripDAO tripDAO;
    private PlanetDAO planetDAO;
    private SpaceportDAO spaceportDAO;
    private TripStateRegistry tripStateRegistry;

    private final Logger logger = LoggerFactory.getLogger(BundleDAOImpl.class);

    @Autowired
    private Draft draft;

    @Autowired
    public TripServiceImpl(TripDAO tripDAO,
                           PlanetDAO planetDAO,
                           SpaceportDAO spaceportDAO,
                           TripStateRegistry tripStateRegistry) {
        this.tripDAO = tripDAO;
        this.planetDAO = planetDAO;
        this.spaceportDAO = spaceportDAO;
        this.tripStateRegistry = tripStateRegistry;
    }

    @Override
    public List<AllCarriersTripsDTO> getAllTripsForCarrier() {
        logger.debug("Getting all trips for carrier from TripDAO");
        //        TODO: implement getting carrier id from access token
        List<Trip> trips = tripDAO.allCarriersTrips(7L);
        logger.debug("Converting trips to DTO");
        List<AllCarriersTripsDTO> tripsDTO = new ArrayList<>();
        for (Trip trip : trips) {
            tripsDTO.add(AllCarriersTripsDTO.from(trip));
        }
        return tripsDTO;
    }

    @Override
    public void saveTrip(TripCreation tripCreation) {
        logger.debug("Saving trip from request DTO");
        Trip trip = parseTripFromTripCreation(tripCreation);
        tripDAO.add(trip);
    }

    @Override
    public Trip updateTrip(User requestUser, TripDTO tripDTO) {
        Optional<Trip> optionalTrip = tripDAO.find(tripDTO.getTripId());

        if (!optionalTrip.isPresent()) {
            throw new RequestException("Illegal operation", HttpStatus.NOT_FOUND);
        } else {
            return updateTrip(requestUser, optionalTrip.get(), tripDTO);
        }
    }

    private Trip updateTrip(User requestUser, Trip trip, TripDTO tripDTO) {
        TripState dtoState = tripStateRegistry.getState(tripDTO.getStatus());

        if (!dtoState.equals(trip.getTripState())) {
            changeStatus(requestUser, trip, dtoState, tripDTO);
        }

        tripDAO.save(trip);
        return trip;
    }

    private void changeStatus(User requestUser, Trip trip, TripState tripState, TripDTO tripDTO) {
        if (!trip.changeStatus(requestUser, tripState, tripDTO)) {
            throw new RequestException("Illegal operation", HttpStatus.FORBIDDEN);
        }
    }

    private Trip parseTripFromTripCreation(TripCreation tripCreation) {
        logger.debug("Converting DTO to Trip");
        Trip trip = new Trip();
        trip.setDepartureDate(tripCreation.getDepartureDateTime());
        trip.setArrivalDate(tripCreation.getArrivalDateTime());
        //        TODO: implement getting carrier id from access token
        trip.setOwner(new User());
        trip.getOwner()
            .setUserId(7);
        trip.setDeparturePlanet(new Planet(planetDAO.getIdByPlanetName(tripCreation.getDeparturePlanet()),
                                           tripCreation.getDeparturePlanet()));
        trip.setArrivalPlanet(new Planet(planetDAO.getIdByPlanetName(tripCreation.getArrivalPlanet()),
                                         tripCreation.getArrivalPlanet()));
        trip.setDepartureSpaceport(new Spaceport(spaceportDAO.getIdBySpaceportName(tripCreation.getDepartureSpaceport()),
                                                 tripCreation.getDepartureSpaceport(),
                                                 trip.getDeparturePlanet()
                                                     .getPlanetId()));
        trip.setArrivalSpaceport(new Spaceport(spaceportDAO.getIdBySpaceportName(tripCreation.getArrivalSpaceport()),
                                               tripCreation.getArrivalSpaceport(),
                                               trip.getArrivalPlanet()
                                                   .getPlanetId()));
        trip.setTripState(draft);
        trip.setCreationDate(LocalDateTime.now());
        trip.setTripPhoto("defaultPhoto.png");
        return trip;
    }
}