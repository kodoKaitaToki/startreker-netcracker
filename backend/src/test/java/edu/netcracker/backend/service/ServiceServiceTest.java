package edu.netcracker.backend.service;

import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.message.response.ServiceDTO;
import edu.netcracker.backend.model.ServiceDescr;
import edu.netcracker.backend.service.impl.ServiceServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceServiceTest {

    @Mock
    ServiceDAO serviceDAO;

    @Mock
    UserService userService;

    @InjectMocks
    ServiceServiceImpl serviceService;

    List<ServiceDTO> ret;

    @Before
    public void setUp() throws Exception {
        ServiceDescr serviceDescr = new ServiceDescr();
        serviceDescr.setServiceId(2L);
        serviceDescr.setServiceName("quis turpis eget");
        serviceDescr.setServiceDescription("amet diam in magna bibendum imperdiet nullam orci pede venenatis non sodales sed tincidunt");
        serviceDescr.setServiceStatus(2);
        serviceDescr.setCreationDate(
                LocalDate.parse(
                        "2015-11-16",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                ).atStartOfDay());
        ServiceDTO testService = ServiceDTO.form(serviceDescr, null);
        ret = new ArrayList<>();
        ret.add(testService);
    }

    @Test
    public void getServicesForApprover() throws Exception {
        when(serviceDAO.getServicesForApprover(0, 10, 2)).thenReturn(ret);

        Assert.assertEquals(serviceService.getServicesForApprover(0, 10, 2, 3), ret);
    }

    @Test
    public void reviewService() throws Exception {
        //TODO: add actual tests
        Assert.assertNull(null);
    }

}