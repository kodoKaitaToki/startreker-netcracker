package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Trip;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = "test")
public class TripDAOTest {

    @Autowired
    private TripDAO tripDAO;

    @Test
    public void testTripDAO() {

        //Creating test trips
        List<Trip> trips = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Trip trip = new Trip(LocalDateTime.of(2019, Month.FEBRUARY, i + 1, 9, 0),
                    LocalDateTime.of(2019, Month.FEBRUARY, i + 2, 9, 0), (long)(i+3),
                    (long)(i+4));

            if (i % 2 == 0) {
                trip.setCreationDate(LocalDate.of(2019, Month.FEBRUARY, i + 5));
            }

            trip.setCreationDate(LocalDate.now());

            trips.add(trip);
        }


        //Saving them
        for (Trip trip : trips) {
            tripDAO.save(trip);
        }

//        //Must be created today ONLY
//        List<Trip> checkList = new ArrayList<>(tripDAO.findByCreationDate(LocalDate.now()));
//
//        for (Trip trip : checkList) {
//            assertThat(trip.getCreationDate(), equalTo(LocalDate.now()));
//        }
//
//        Trip trip1 = checkList.get(0);
//        Long newArrivalId = 12345L;
//        trip1.setArrivalId(newArrivalId);
//        tripDAO.update(trip1);
//
//        Trip check = tripDAO.find(trip1.getTripId()).get();
//        assertThat(check.getDepartureId(), equalTo(trip1.getDepartureId()));

//        assertThat(tripDAO.count(), equalTo(BigInteger.valueOf(5L)));

    }
}
