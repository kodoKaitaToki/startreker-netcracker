package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.message.request.ServicePendingActivationDto;
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
@SuppressWarnings("Duplicates")
public class ServicePendingDaoTest {

    @Mock
    private ServicePendingDao servicePendingDao;

    private List<ServicePendingActivationDto> expectedServicesListQueryLimit_1PendingActivationDto;

    private List<ServicePendingActivationDto> expectedServicesListQueryLimit_2PendingActivationDto;

    private List<ServicePendingActivationDto> expectedServicesListQueryLimit_1_Offset_1PendingActivationDto;

    @Before
    public void init() {

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
    public void getPendingWithOffsetAndLimit() {

        when(servicePendingDao.getPendingWithOffsetAndLimit(1, 0)).thenReturn(expectedServicesListQueryLimit_1PendingActivationDto);
        when(servicePendingDao.getPendingWithOffsetAndLimit(2, 0)).thenReturn(expectedServicesListQueryLimit_2PendingActivationDto);
        when(servicePendingDao.getPendingWithOffsetAndLimit(1, 1)).thenReturn(expectedServicesListQueryLimit_1_Offset_1PendingActivationDto);

        assertEquals(servicePendingDao.getPendingWithOffsetAndLimit(1, 0), expectedServicesListQueryLimit_1PendingActivationDto);
        assertEquals(servicePendingDao.getPendingWithOffsetAndLimit(2, 0), expectedServicesListQueryLimit_2PendingActivationDto);
        assertEquals(servicePendingDao.getPendingWithOffsetAndLimit(1, 1), expectedServicesListQueryLimit_1_Offset_1PendingActivationDto);
    }
}