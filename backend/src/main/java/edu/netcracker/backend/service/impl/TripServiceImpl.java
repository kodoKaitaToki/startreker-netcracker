package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.PlanetDAO;
import edu.netcracker.backend.dao.SpaceportDAO;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.message.request.trips.TripCreation;
import edu.netcracker.backend.message.response.trips.AllCarriersTripsDTO;
import edu.netcracker.backend.model.Planet;
import edu.netcracker.backend.model.Spaceport;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.state.trip.Draft;
import edu.netcracker.backend.model.state.trip.TripStateRegistry;
import edu.netcracker.backend.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TripServiceImpl implements TripService {

    private TripDAO tripDAO;
    private PlanetDAO planetDAO;
    private SpaceportDAO spaceportDAO;
    private TripStateRegistry tripStateRegistry;

    @Autowired
    private Draft draft;

    @Autowired
    public TripServiceImpl(TripDAO tripDAO, PlanetDAO planetDAO, SpaceportDAO spaceportDAO, TripStateRegistry tripStateRegistry) {
        this.tripDAO = tripDAO;
        this.planetDAO = planetDAO;
        this.spaceportDAO = spaceportDAO;
        this.tripStateRegistry = tripStateRegistry;
    }

    @Override
    public List<AllCarriersTripsDTO> getAllTripsForCarrier() {
        //        TODO: implement getting carrier id from access token
        List<Trip> trips = tripDAO.allCarriersTrips(7L);
        List<AllCarriersTripsDTO> tripsDTO = new ArrayList<>();
        for (Trip trip: trips) {
            tripsDTO.add(AllCarriersTripsDTO.from(trip));
        }
        return tripsDTO;
    }

    @Override
    public void saveTrip(TripCreation tripCreation) {
        Trip trip = parseTripFromTripCreation(tripCreation);
        tripDAO.save(trip);
    }

    private Trip parseTripFromTripCreation(TripCreation tripCreation) {
        Trip trip = new Trip();
        trip.setDepartureDate(tripCreation.getDepartureDateTime());
        trip.setArrivalDate(tripCreation.getArrivalDateTime());
//        TODO: implement getting carrier id from access token
        trip.setCarrierId(7L);
        trip.setDeparturePlanet(new Planet(planetDAO.getIdByPlanetName(tripCreation.getDeparturePlanet()), tripCreation.getDeparturePlanet()));
        trip.setArrivalPlanet(new Planet(planetDAO.getIdByPlanetName(tripCreation.getArrivalPlanet()), tripCreation.getArrivalPlanet()));
        trip.setDepartureSpaceport(new Spaceport(spaceportDAO.getIdBySpaceportName(tripCreation.getDepartureSpaceport()),
                tripCreation.getDepartureSpaceport(), trip.getDeparturePlanet().getPlanetId()));
        trip.setArrivalSpaceport(new Spaceport(spaceportDAO.getIdBySpaceportName(tripCreation.getArrivalSpaceport()), tripCreation.getArrivalSpaceport(),
                trip.getArrivalPlanet().getPlanetId()));
        trip.setTripState(draft);
        trip.setCreationDate(LocalDateTime.now());
        trip.setTripPhoto("defaultPhoto.png");
        return trip;
    }
}
