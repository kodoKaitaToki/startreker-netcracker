package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.message.request.PendingActivationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("Duplicates")
public class ServicePendingSrvcTest {

    @Mock
    private ServicePendingSrvc servicePendingSrvc;

    private List<PendingActivationService> expectedPendingActivationServicesListQueryLimit_1;

    private List<PendingActivationService> expectedPendingActivationServicesListQueryLimit_2;

    private List<PendingActivationService> expectedPendingActivationServicesListQueryLimit_1_Offset_1;

    @Before
    public void init() {

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
    public void getPendingWithOffsetAndLimit() {

        when(servicePendingSrvc.getPendingWithOffsetAndLimit(1, 0)).thenReturn(expectedPendingActivationServicesListQueryLimit_1);
        when(servicePendingSrvc.getPendingWithOffsetAndLimit(2, 0)).thenReturn(expectedPendingActivationServicesListQueryLimit_2);
        when(servicePendingSrvc.getPendingWithOffsetAndLimit(1, 1)).thenReturn(expectedPendingActivationServicesListQueryLimit_1_Offset_1);

        assertEquals(servicePendingSrvc.getPendingWithOffsetAndLimit(1, 0), expectedPendingActivationServicesListQueryLimit_1);
        assertEquals(servicePendingSrvc.getPendingWithOffsetAndLimit(2, 0), expectedPendingActivationServicesListQueryLimit_2);
        assertEquals(servicePendingSrvc.getPendingWithOffsetAndLimit(1, 1), expectedPendingActivationServicesListQueryLimit_1_Offset_1);

    }
}