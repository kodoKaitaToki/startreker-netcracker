package edu.netcracker.backend.service;

import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.dao.UserDAO;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.security.SecurityContext;
import edu.netcracker.backend.service.impl.TripServiceImpl;
import edu.netcracker.backend.utils.TripStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = "test")
public class TripServiceTest {

    @Autowired
    private TripDAO tripDAO;

    @Autowired
    private UserDAO userDAO;

    private TripService tripService;

    @PostConstruct
    public void init(){
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        User user = userDAO.find(16L).get();
        when(mockSecurityContext.getUser()).thenReturn(user);
        tripService = new TripServiceImpl(
                tripDAO,
                mockSecurityContext
        );
    }

    @Test
    public void normalLifecycle(){
        long tripId = 5L;
        setStatus(tripId, TripStatus.DRAFT);
        tripService.open(tripId);
        assertStatus(tripId, TripStatus.OPEN);
        tripService.assign(tripId);
        assertStatus(tripId, TripStatus.ASSIGNED);
        tripService.publish(tripId);
        assertStatus(tripId, TripStatus.PUBLISHED);
        tripService.archive(tripId);
        assertStatus(tripId, TripStatus.ARCHIVED);
        tripService.remove(tripId);
        assertStatus(tripId, TripStatus.REMOVED);
    }

    @Test
    public void lifecycleTest(){
        test(TripStatus.DRAFT, 5L, TripStatus.OPEN);
        test(TripStatus.OPEN, 5L, TripStatus.ASSIGNED, TripStatus.REMOVED);
        test(TripStatus.ASSIGNED, 5L, TripStatus.PUBLISHED, TripStatus.UNDER_CLARIFICATION, TripStatus.REMOVED);
        test(TripStatus.PUBLISHED, 5L, TripStatus.ARCHIVED, TripStatus.UNDER_CLARIFICATION, TripStatus.REMOVED);
        test(TripStatus.ARCHIVED, 5L, TripStatus.OPEN);
        //test(TripStatus.UNDER_CLARIFICATION, 5L, TripStatus.OPEN, TripStatus.UNDER_CLARIFICATION);
        test(TripStatus.REMOVED, 5L);
    }

    private void test(TripStatus status, long id, TripStatus ... expectedToSucceed){
        List<TripStatus> expected = Arrays.asList(expectedToSucceed);
        try{
            setStatus(id, status);
            tripService.open(id);
            if(!expected.contains(TripStatus.OPEN)) fail();
        }catch (Exception e){
            if(expected.contains(TripStatus.OPEN)){
                fail();
            }
        }
        try{
            setStatus(id, status);
            tripService.assign(id);
            if(!expected.contains(TripStatus.ASSIGNED)) fail();
        }catch (Exception e){
            if(expected.contains(TripStatus.ASSIGNED)){
                fail();
            }
        }
        try{
            setStatus(id, status);
            tripService.publish(id);
            if(!expected.contains(TripStatus.PUBLISHED)) fail();
        }catch (Exception e){
            if(expected.contains(TripStatus.PUBLISHED)){
                fail();
            }
        }
        try{
            setStatus(id, status);
            tripService.archive(id);
            if(!expected.contains(TripStatus.ARCHIVED)) fail();
        }catch (Exception e){
            if(expected.contains(TripStatus.ARCHIVED)){
                fail();
            }
        }
        /*try{
            setStatus(id, status);
            tripService.clarify(id);
            if(!expected.contains(6L)) fail();
        }catch (Exception e){
            if(expected.contains(6L)){
                fail();
            }
        }*/
        try{
            setStatus(id, status);
            tripService.remove(id);
            if(!expected.contains(TripStatus.REMOVED)) fail();
        }catch (Exception e){
            if(expected.contains(TripStatus.REMOVED)){
                fail();
            }
        }
    }

    private void setStatus(long id, TripStatus status){
        Trip trip = tripDAO.find(id).get();
        trip.setTripStatus(status.getValue());
        tripDAO.save(trip);
    }

    private void assertStatus(long id, TripStatus status){
        Trip trip = tripDAO.find(id).get();
        if(trip.getTripStatus() != status.getValue()){
            throw new AssertionError(
                    "Expected status: " + status.getValue()
                            + " actual: " + trip.getTripStatus());
        }
    }
}
