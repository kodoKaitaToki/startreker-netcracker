package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.request.PendingActivationService;
import edu.netcracker.backend.message.request.PendingActivationTrip;
import edu.netcracker.backend.model.Planet;
import edu.netcracker.backend.model.Spaceport;
import edu.netcracker.backend.service.impl.ServicePendingSrvc;
import edu.netcracker.backend.service.impl.TripPendingSrvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("ALL")
public class PendingTripServiceControllerTest {

    @Mock
    private ServicePendingSrvc servicePendingSrvc;

    @Mock
    private TripPendingSrvc tripPendingSrvc;

    private List<PendingActivationTrip> pendingActivationTripList;

    private List<PendingActivationService> expectedPendingActivationServicesListQueryLimit_1;

    private List<PendingActivationService> expectedPendingActivationServicesListQueryLimit_2;

    private List<PendingActivationService> expectedPendingActivationServicesListQueryLimit_1_Offset_1;


    @Before
    public void init() {

        PendingActivationTrip pendingActivationTrip = PendingActivationTrip.builder()
                .tripID(14)
                .tripStatus("1")
                .arrivalDate("2019-03-13 00:12:00.000000")
                .departureDate("2019-03-14 23:04:00.000000")
                .creationDate("2019-03-12 12:03:38.853000")
                .approverName(null)
                .approverEmail(null)
                .approverTel(null)
                .carrierName("mstebles6")
                .carrierEmail("aissacof6@nih.gov")
                .carrierTel("2111984543")
                .departmentPlanetName("MOON")
                .departmentSpaceportName("porta")
                .arrivalPlanetName("VENUS")
                .arrivalSpaceportName("erat")
                .build();

        pendingActivationTripList = Arrays.asList(pendingActivationTrip);

        PendingActivationService firstPendingActivationService = PendingActivationService.builder()
                .serviceId(12)
                .serviceName("new_service")
                .serviceDescr("sapien urna pretium nisl ut volutpat sapien arcu sed augue aliquam")
                .serviceStatus("1")
                .creationDate("2019-03-05 10:57:01.026000")
                .approverName(null)
                .approverEmail(null)
                .approverTel(null)
                .carrierName("mstebles6")
                .carrierEmail("aissacof6@nih.gov")
                .carrierTel("2111984543")
                .build();

        PendingActivationService secondPendingActivationService = PendingActivationService.builder()
                .serviceId(20)
                .serviceName("draft")
                .serviceDescr("draft_description")
                .serviceStatus("1")
                .creationDate("2019-03-05 11:48:14.627000")
                .approverName(null)
                .approverEmail(null)
                .approverTel(null)
                .carrierName("mstebles6")
                .carrierEmail("aissacof6@nih.gov")
                .carrierTel("2111984543")
                .build();

        expectedPendingActivationServicesListQueryLimit_1 = Arrays.asList(firstPendingActivationService);
        expectedPendingActivationServicesListQueryLimit_2 = Arrays.asList(firstPendingActivationService, secondPendingActivationService);
        expectedPendingActivationServicesListQueryLimit_1_Offset_1 = Arrays.asList(secondPendingActivationService);
    }

    @Test
    public void getServicesLimitOffset() {

        when(servicePendingSrvc.getPendingWithOffsetAndLimit(1, 0)).thenReturn(expectedPendingActivationServicesListQueryLimit_1);
        when(servicePendingSrvc.getPendingWithOffsetAndLimit(2, 0)).thenReturn(expectedPendingActivationServicesListQueryLimit_2);
        when(servicePendingSrvc.getPendingWithOffsetAndLimit(1, 1)).thenReturn(expectedPendingActivationServicesListQueryLimit_1_Offset_1);

        assertEquals(servicePendingSrvc.getPendingWithOffsetAndLimit(1, 0), expectedPendingActivationServicesListQueryLimit_1);
        assertEquals(servicePendingSrvc.getPendingWithOffsetAndLimit(2, 0), expectedPendingActivationServicesListQueryLimit_2);
        assertEquals(servicePendingSrvc.getPendingWithOffsetAndLimit(1, 1), expectedPendingActivationServicesListQueryLimit_1_Offset_1);
    }

    @Test
    public void getTripsLimitOffset() {

        when(tripPendingSrvc.getPendingWithOffsetAndLimit(1, 0)).thenReturn(pendingActivationTripList);
        assertEquals(tripPendingSrvc.getPendingWithOffsetAndLimit(1, 0), pendingActivationTripList);
    }
}