package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.request.ServicePendingActivationDto;
import edu.netcracker.backend.message.request.TripPendingActivationDto;
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

    private List<TripPendingActivationDto> tripPendingActivationDtoList;

    private List<ServicePendingActivationDto> expectedServicesListQueryLimit_1PendingActivationDto;

    private List<ServicePendingActivationDto> expectedServicesListQueryLimit_2PendingActivationDto;

    private List<ServicePendingActivationDto> expectedServicesListQueryLimit_1_Offset_1PendingActivationDto;


    @Before
    public void init() {

        TripPendingActivationDto tripPendingActivationDto = TripPendingActivationDto.builder()
                .tripID(14)
                .tripStatus(1)
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

        tripPendingActivationDtoList = Arrays.asList(tripPendingActivationDto);

        ServicePendingActivationDto firstServicePendingActivationDto = ServicePendingActivationDto.builder()
                .serviceId(12)
                .serviceName("new_service")
                .serviceDescr("sapien urna pretium nisl ut volutpat sapien arcu sed augue aliquam")
                .serviceStatus(1)
                .creationDate("2019-03-05 10:57:01.026000")
                .approverName(null)
                .approverEmail(null)
                .approverTel(null)
                .carrierName("mstebles6")
                .carrierEmail("aissacof6@nih.gov")
                .carrierTel("2111984543")
                .build();

        ServicePendingActivationDto secondServicePendingActivationDto = ServicePendingActivationDto.builder()
                .serviceId(20)
                .serviceName("draft")
                .serviceDescr("draft_description")
                .serviceStatus(1)
                .creationDate("2019-03-05 11:48:14.627000")
                .approverName(null)
                .approverEmail(null)
                .approverTel(null)
                .carrierName("mstebles6")
                .carrierEmail("aissacof6@nih.gov")
                .carrierTel("2111984543")
                .build();

        expectedServicesListQueryLimit_1PendingActivationDto = Arrays.asList(firstServicePendingActivationDto);
        expectedServicesListQueryLimit_2PendingActivationDto = Arrays.asList(firstServicePendingActivationDto, secondServicePendingActivationDto);
        expectedServicesListQueryLimit_1_Offset_1PendingActivationDto = Arrays.asList(secondServicePendingActivationDto);
    }

    @Test
    public void getServicesLimitOffset() {

        when(servicePendingSrvc.getPendingWithOffsetAndLimit(1, 0)).thenReturn(expectedServicesListQueryLimit_1PendingActivationDto);
        when(servicePendingSrvc.getPendingWithOffsetAndLimit(2, 0)).thenReturn(expectedServicesListQueryLimit_2PendingActivationDto);
        when(servicePendingSrvc.getPendingWithOffsetAndLimit(1, 1)).thenReturn(expectedServicesListQueryLimit_1_Offset_1PendingActivationDto);

        assertEquals(servicePendingSrvc.getPendingWithOffsetAndLimit(1, 0), expectedServicesListQueryLimit_1PendingActivationDto);
        assertEquals(servicePendingSrvc.getPendingWithOffsetAndLimit(2, 0), expectedServicesListQueryLimit_2PendingActivationDto);
        assertEquals(servicePendingSrvc.getPendingWithOffsetAndLimit(1, 1), expectedServicesListQueryLimit_1_Offset_1PendingActivationDto);
    }

    @Test
    public void getTripsLimitOffset() {

        when(tripPendingSrvc.getPendingWithOffsetAndLimit(1, 0)).thenReturn(tripPendingActivationDtoList);
        assertEquals(tripPendingSrvc.getPendingWithOffsetAndLimit(1, 0), tripPendingActivationDtoList);
    }
}