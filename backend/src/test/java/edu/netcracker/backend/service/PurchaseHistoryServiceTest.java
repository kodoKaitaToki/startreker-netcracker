package edu.netcracker.backend.service;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.dao.TicketDAO;
import edu.netcracker.backend.message.response.HistoryDTO.HistoryServiceDTO;
import edu.netcracker.backend.message.response.HistoryDTO.HistoryTicketDTO;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.model.history.HistoryService;
import edu.netcracker.backend.model.history.HistoryTicket;
import edu.netcracker.backend.model.history.HistoryTrip;
import edu.netcracker.backend.security.SecurityContext;
import edu.netcracker.backend.service.impl.PurchaseHistoryServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PurchaseHistoryServiceTest {

    @Mock
    private TicketDAO ticketDAO;

    @Mock
    private ServiceDAO serviceDAO;

    @Mock
    private SecurityContext securityContext;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private PurchaseHistoryService purchaseHistoryService;
    private List<HistoryTicketDTO> expectedTicketResult;
    private List<HistoryServiceDTO> expectedServiceResult;

    @Before
    public void startUp() {
        MockitoAnnotations.initMocks(this);
        HistoryTicket ticket1 = new HistoryTicket();
        HistoryTicket ticket2 = new HistoryTicket();
        HistoryTrip trip = new HistoryTrip();
        ticket1.setTicketId(1);
        ticket1.setSeat(1);
        ticket1.setEndPrice(Float.MIN_VALUE);
        ticket1.setPurchaseDate(LocalDateTime.MIN);
        ticket1.setClassName("a");
        ticket1.setBundleId(0);
        ticket2.setTicketId(2);
        ticket2.setSeat(2);
        ticket2.setEndPrice(Float.MIN_VALUE);
        ticket2.setPurchaseDate(LocalDateTime.MIN);
        ticket2.setBundleId(0);
        trip.setDepartureDate(LocalDateTime.MIN);
        trip.setArrivalDate(LocalDateTime.MAX);
        trip.setDepartureSpaceportName("a");
        trip.setArrivalSpaceportName("b");
        trip.setDeparturePlanetName("aa");
        trip.setArrivalPlanetName("bb");
        trip.setCarrierName("aabb");
        ticket1.setTrip(trip);
        ticket2.setTrip(trip);
        when(ticketDAO.findAllPurchasedByUser(any(), any(), any(), any(), any())).thenReturn(Arrays.asList(ticket1,
                                                                                                           ticket2));

        HistoryService service1 = new HistoryService();
        service1.setServiceName("a");
        service1.setServiceCount(1);
        HistoryService service2 = new HistoryService();
        service2.setServiceName("b");
        service2.setServiceCount(1);
        when(serviceDAO.getServiceNamesByTicket(any())).thenReturn(Arrays.asList(service1, service2));

        User user = new User();
        user.setUserId(1);
        when(securityContext.getUser()).thenReturn(user);

        purchaseHistoryService = new PurchaseHistoryServiceImpl(ticketDAO, serviceDAO, securityContext);

        expectedTicketResult = Arrays.asList(HistoryTicketDTO.from(ticket1), HistoryTicketDTO.from(ticket2));
        expectedServiceResult = Arrays.asList(HistoryServiceDTO.from(service1), HistoryServiceDTO.from(service2));
    }

    @Test
    public void getPurchaseHistory() {
        Assert.assertEquals(purchaseHistoryService.getPurchaseHistory(1, 0, null, null), expectedTicketResult);
    }

    @Test
    public void checkNullLimit() {
        expectedEx.expect(RequestException.class);
        expectedEx.expectMessage("Limit was null");

        purchaseHistoryService.getPurchaseHistory(null, 1, null, null);
    }

    @Test
    public void checkNullOffset() {
        expectedEx.expect(RequestException.class);
        expectedEx.expectMessage("Offset was null");

        purchaseHistoryService.getPurchaseHistory(1, null, null, null);
    }

    @Test
    public void getServiceNamesByTicket() {
        Assert.assertEquals(purchaseHistoryService.getServiceNamesByTicket(1), expectedServiceResult);
    }

    @Test
    public void checkNullTicketId() {
        expectedEx.expect(RequestException.class);
        expectedEx.expectMessage("Ticket id was null");

        purchaseHistoryService.getServiceNamesByTicket(null);
    }
}
