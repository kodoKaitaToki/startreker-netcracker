package edu.netcracker.backend.service;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.PlanetDAO;
import edu.netcracker.backend.dao.SpaceportDAO;
import edu.netcracker.backend.dao.TripDAO;
import edu.netcracker.backend.dao.TripReplyDAO;
import edu.netcracker.backend.message.response.TripDTO;
import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.Trip;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.model.state.trip.*;
import edu.netcracker.backend.service.impl.TripServiceImpl;
import edu.netcracker.backend.utils.AuthorityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = "test")
public class TripServiceImplTest {

    @Mock
    private TripDAO tripDAOMock;

    @Autowired
    private TripStateRegistry tripStateRegistry;

    @Autowired
    private TripReplyDAO tripReplyDAO;

    @Autowired
    private PlanetDAO planetDAO;

    @Autowired
    private SpaceportDAO spaceportDAO;

    private TripService tripService;

    private User approver;
    private User carrier;
    private User illegalApprover;
    private User illegalCarrier;

    private Trip draftTrip;
    private Trip openTrip;
    private Trip assignedTrip;
    private Trip publishedTrip;
    private Trip archivedTrip;
    private Trip underClarificationTrip;
    private Trip removedTrip;

    private TripDTO draftTripDTO;
    private TripDTO openTripDTO;
    private TripDTO assignedTripDTO;
    private TripDTO publishedTripDTO;
    private TripDTO archivedTripDTO;
    private TripDTO underClarificationTripDTO;
    private TripDTO removedTripDTO;

    @PostConstruct
    public void init() {
        List<Role> approverRole = new ArrayList<>();
        approverRole.add(AuthorityUtils.ROLE_APPROVER);
        List<Role> carrierRole = new ArrayList<>();
        approverRole.add(AuthorityUtils.ROLE_CARRIER);
        approver = new User();
        approver.setUserId(1);
        approver.setUserRoles(approverRole);
        carrier = new User();
        carrier.setUserId(2);
        carrier.setUserRoles(carrierRole);
        illegalApprover = new User();
        illegalApprover.setUserId(3);
        illegalApprover.setUserRoles(approverRole);
        illegalCarrier = new User();
        illegalCarrier.setUserId(4);
        illegalCarrier.setUserRoles(carrierRole);

        draftTrip = new Trip();
        draftTrip.setTripId(1L);
        draftTrip.setApprover(approver);
        draftTrip.setOwner(carrier);
        draftTrip.setTripState(new Draft());

        openTrip = new Trip();
        openTrip.setTripId(1L);
        openTrip.setApprover(approver);
        openTrip.setOwner(carrier);
        openTrip.setTripState(new Open());

        assignedTrip = new Trip();
        assignedTrip.setTripId(1L);
        assignedTrip.setApprover(approver);
        assignedTrip.setOwner(carrier);
        assignedTrip.setTripState(new Assigned());

        publishedTrip = new Trip();
        publishedTrip.setTripId(1L);
        publishedTrip.setApprover(approver);
        publishedTrip.setOwner(carrier);
        publishedTrip.setTripState(new Published());

        archivedTrip = new Trip();
        archivedTrip.setTripId(1L);
        archivedTrip.setApprover(approver);
        archivedTrip.setOwner(carrier);
        archivedTrip.setTripState(new Archived());

        underClarificationTrip = new Trip();
        underClarificationTrip.setTripId(1L);
        underClarificationTrip.setApprover(approver);
        underClarificationTrip.setOwner(carrier);
        underClarificationTrip.setTripState(new UnderClarification(tripReplyDAO));

        removedTrip = new Trip();
        removedTrip.setTripId(1L);
        removedTrip.setApprover(approver);
        removedTrip.setOwner(carrier);
        removedTrip.setTripState(new Removed());

        draftTripDTO = TripDTO.from(draftTrip);
        openTripDTO = TripDTO.from(openTrip);
        assignedTripDTO = TripDTO.from(assignedTrip);
        publishedTripDTO = TripDTO.from(publishedTrip);
        archivedTripDTO = TripDTO.from(archivedTrip);
        underClarificationTripDTO = TripDTO.from(underClarificationTrip);
        underClarificationTripDTO.setReply("test reply");
        removedTripDTO = TripDTO.from(removedTrip);

        tripService = new TripServiceImpl(tripDAOMock, planetDAO, spaceportDAO, tripStateRegistry);
    }

    // Draft tests

    @Test(expected = RequestException.class)
    public void draftToDraftTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(draftTrip));
        tripService.updateTrip(carrier, draftTripDTO);
    }

    @Test
    public void draftToOpenTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(draftTrip));
        tripService.updateTrip(carrier, openTripDTO);
    }

    @Test(expected = RequestException.class)
    public void draftToAssignedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(draftTrip));
        tripService.updateTrip(approver, assignedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void draftToPublishedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(draftTrip));
        tripService.updateTrip(approver, publishedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void draftToArchivedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(draftTrip));
        tripService.updateTrip(carrier, archivedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void draftToUnderClarificationTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(draftTrip));
        tripService.updateTrip(approver, underClarificationTripDTO);
    }

    @Test
    public void draftToRemovedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(draftTrip));
        tripService.updateTrip(approver, removedTripDTO);
    }


    // Open tests


    @Test(expected = RequestException.class)
    public void openToOpenTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(openTrip));
        tripService.updateTrip(carrier, openTripDTO);
    }

    @Test(expected = RequestException.class)
    public void openToDraftTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(openTrip));
        tripService.updateTrip(carrier, draftTripDTO);
    }

    @Test
    public void openToAssignedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(openTrip));
        tripService.updateTrip(approver, assignedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void openToPublishedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(openTrip));
        tripService.updateTrip(approver, publishedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void openToArchivedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(openTrip));
        tripService.updateTrip(carrier, archivedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void openToUnderClarificationTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(openTrip));
        tripService.updateTrip(approver, underClarificationTripDTO);
    }

    @Test
    public void openToRemovedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(openTrip));
        tripService.updateTrip(approver, removedTripDTO);
    }


    // Assigned test


    @Test(expected = RequestException.class)
    public void assignedToOpenTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(assignedTrip));
        tripService.updateTrip(carrier, openTripDTO);
    }

    @Test(expected = RequestException.class)
    public void assignedToDraftTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(assignedTrip));
        tripService.updateTrip(carrier, draftTripDTO);
    }

    @Test(expected = RequestException.class)
    public void assignedToAssignedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(assignedTrip));
        tripService.updateTrip(approver, assignedTripDTO);
    }

    @Test
    public void assignedToPublishedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(assignedTrip));
        tripService.updateTrip(approver, publishedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void assignedToArchivedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(assignedTrip));
        tripService.updateTrip(carrier, archivedTripDTO);
    }

    @Test
    public void assignedToUnderClarificationTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(assignedTrip));
        tripService.updateTrip(approver, underClarificationTripDTO);
    }

    @Test
    public void assignedToRemovedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(assignedTrip));
        tripService.updateTrip(approver, removedTripDTO);
    }


    // Published tests


    @Test(expected = RequestException.class)
    public void publishedToOpenTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(publishedTrip));
        tripService.updateTrip(carrier, openTripDTO);
    }

    @Test(expected = RequestException.class)
    public void publishedToDraftTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(publishedTrip));
        tripService.updateTrip(carrier, draftTripDTO);
    }

    @Test(expected = RequestException.class)
    public void publishedToAssignedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(publishedTrip));
        tripService.updateTrip(approver, assignedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void publishedToPublishedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(publishedTrip));
        tripService.updateTrip(approver, publishedTripDTO);
    }

    @Test
    public void publishedToArchivedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(publishedTrip));
        tripService.updateTrip(carrier, archivedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void publishedToUnderClarificationTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(publishedTrip));
        tripService.updateTrip(approver, underClarificationTripDTO);
    }

    @Test
    public void publishedToRemovedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(publishedTrip));
        tripService.updateTrip(approver, removedTripDTO);
    }


    // Archived tests


    @Test
    public void archivedToOpenTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(archivedTrip));
        tripService.updateTrip(carrier, openTripDTO);
    }

    @Test(expected = RequestException.class)
    public void archivedToDraftTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(archivedTrip));
        tripService.updateTrip(carrier, draftTripDTO);
    }

    @Test(expected = RequestException.class)
    public void archivedToAssignedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(archivedTrip));
        tripService.updateTrip(approver, assignedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void archivedToPublishedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(archivedTrip));
        tripService.updateTrip(approver, publishedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void archivedToArchivedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(archivedTrip));
        tripService.updateTrip(carrier, archivedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void archivedToUnderClarificationTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(archivedTrip));
        tripService.updateTrip(approver, underClarificationTripDTO);
    }

    @Test
    public void archivedToRemovedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(archivedTrip));
        tripService.updateTrip(approver, removedTripDTO);
    }


    // Under clarification tests


    @Test
    public void underClarificationToOpenTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(underClarificationTrip));
        tripService.updateTrip(carrier, openTripDTO);
    }

    @Test(expected = RequestException.class)
    public void underClarificationToDraftTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(underClarificationTrip));
        tripService.updateTrip(carrier, draftTripDTO);
    }

    @Test(expected = RequestException.class)
    public void underClarificationToAssignedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(underClarificationTrip));
        tripService.updateTrip(approver, assignedTripDTO);
    }

    @Test
    public void underClarificationToPublishedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(underClarificationTrip));
        tripService.updateTrip(approver, publishedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void underClarificationToArchivedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(underClarificationTrip));
        tripService.updateTrip(carrier, archivedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void underClarificationToUnderClarificationTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(underClarificationTrip));
        tripService.updateTrip(approver, underClarificationTripDTO);
    }

    @Test
    public void underClarificationToRemovedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(underClarificationTrip));
        tripService.updateTrip(approver, removedTripDTO);
    }


    // Removed tests


    @Test(expected = RequestException.class)
    public void removedToOpenTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(removedTrip));
        tripService.updateTrip(carrier, openTripDTO);
    }

    @Test(expected = RequestException.class)
    public void removedClarificationToDraftTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(removedTrip));
        tripService.updateTrip(carrier, draftTripDTO);
    }

    @Test(expected = RequestException.class)
    public void removedToAssignedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(removedTrip));
        tripService.updateTrip(approver, assignedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void removedToPublishedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(removedTrip));
        tripService.updateTrip(approver, publishedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void removedToArchivedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(removedTrip));
        tripService.updateTrip(carrier, archivedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void removedToUnderClarificationTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(removedTrip));
        tripService.updateTrip(approver, underClarificationTripDTO);
    }

    @Test(expected = RequestException.class)
    public void removedToRemovedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(removedTrip));
        tripService.updateTrip(approver, removedTripDTO);
    }


    // Security tests


    @Test(expected = RequestException.class)
    public void illegalDraftToOpenTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(draftTrip));
        tripService.updateTrip(illegalCarrier, openTripDTO);
    }

    @Test(expected = RequestException.class)
    public void illegalDraftToRemovedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(draftTrip));
        tripService.updateTrip(illegalApprover, removedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void illegalOpenToAssignedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(openTrip));
        tripService.updateTrip(carrier, assignedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void illegalOpenToRemovedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(openTrip));
        tripService.updateTrip(illegalApprover, removedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void illegalUnderClarificationToRemovedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(underClarificationTrip));
        tripService.updateTrip(illegalApprover, removedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void illegalAssignedToUnderClarificationTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(assignedTrip));
        tripService.updateTrip(illegalApprover, underClarificationTripDTO);
    }

    @Test(expected = RequestException.class)
    public void illegalAssignedToRemovedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(assignedTrip));
        tripService.updateTrip(illegalApprover, removedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void illegalPublishedToRemovedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(publishedTrip));
        tripService.updateTrip(illegalApprover, removedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void illegalAssignedToPublishedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(assignedTrip));
        tripService.updateTrip(illegalApprover, publishedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void illegalPublishedToArchivedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(publishedTrip));
        tripService.updateTrip(illegalCarrier, archivedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void illegalArchivedToOpenTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(archivedTrip));
        tripService.updateTrip(illegalCarrier, openTripDTO);
    }

    @Test(expected = RequestException.class)
    public void illegalArchivedToRemovedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(archivedTrip));
        tripService.updateTrip(illegalApprover, removedTripDTO);
    }

    @Test(expected = RequestException.class)
    public void illegalUnderClarificationToOpenTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(underClarificationTrip));
        tripService.updateTrip(illegalCarrier, openTripDTO);
    }

    @Test(expected = RequestException.class)
    public void illegalUnderClarificationToPublishedTest() {
        when(tripDAOMock.find(1L)).thenReturn(Optional.of(underClarificationTrip));
        tripService.updateTrip(illegalApprover, publishedTripDTO);
    }
}