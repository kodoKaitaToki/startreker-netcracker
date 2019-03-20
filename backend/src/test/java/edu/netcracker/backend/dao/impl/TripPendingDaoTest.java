package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.message.request.TripPendingActivationDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SuppressWarnings("ALL")
@RunWith(MockitoJUnitRunner.class)
public class TripPendingDaoTest {

    private List<TripPendingActivationDto> tripPendingActivationDtoList;

    @Mock
    private TripPendingDao tripPendingDao;

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
    }

    @Test
    public void getPendingWithOffsetAndLimit() {

        when(tripPendingDao.getPendingWithOffsetAndLimit(1, 0)).thenReturn(tripPendingActivationDtoList);
        assertEquals(tripPendingDao.getPendingWithOffsetAndLimit(1, 0), tripPendingActivationDtoList);
    }
}