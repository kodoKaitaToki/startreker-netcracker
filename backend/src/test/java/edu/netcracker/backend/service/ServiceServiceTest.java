package edu.netcracker.backend.service;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.message.request.ServiceCreateForm;
import edu.netcracker.backend.message.response.ServiceCRUDDTO;
import edu.netcracker.backend.model.ServiceDescr;
import edu.netcracker.backend.service.impl.ServiceServiceImpl;
import edu.netcracker.backend.utils.ServiceStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceServiceTest {

    @Mock
    ServiceDAO serviceDAO;

    @Mock
    UserService userService;

    @InjectMocks
    ServiceServiceImpl serviceService;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    List<ServiceCRUDDTO> ret;
    private ServiceCreateForm serviceCreateForm = new ServiceCreateForm();
    private ServiceDescr serviceDescr = new ServiceDescr();
    private ServiceCRUDDTO serviceCRUDDTO = new ServiceCRUDDTO();

    @Before
    public void setUp() throws Exception {
        serviceDescr.setServiceId(2L);
        serviceDescr.setServiceName("quis turpis eget");
        serviceDescr.setServiceDescription(
                "amet diam in magna bibendum imperdiet nullam orci pede venenatis non sodales sed tincidunt");
        serviceDescr.setServiceStatus(2);
        serviceDescr.setCreationDate(LocalDate.parse("2015-11-16", DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                              .atStartOfDay());
        ServiceCRUDDTO testService = ServiceCRUDDTO.form(serviceDescr, null);
        ret = new ArrayList<>();
        ret.add(testService);
    }

    @Before
    public void setCreateForm(){
        serviceCreateForm.setServiceName("namenamename");
        serviceCreateForm.setServiceDescription("description");
        serviceCreateForm.setServiceStatus("UNDER_CLARIFICATION");
    }

    @Before
    public void setServiceCRUDDTO(){
        serviceCRUDDTO.setId(2L);
        serviceCRUDDTO.setServiceName("quis turpis eget");
        serviceCRUDDTO.setServiceDescription(
                "amet diam in magna bibendum imperdiet nullam orci pede venenatis non sodales sed tincidunt");
        serviceCRUDDTO.setCreationDate("2015-11-16");
        serviceCRUDDTO.setApproverName("");
        serviceCRUDDTO.setReplyText("");
        serviceCRUDDTO.setServiceStatus("OPEN");
    }

    @Test
    public void getServicesForApprover() throws Exception {
        when(serviceDAO.getServicesForApprover(0, 10, 2)).thenReturn(ret);

        Assert.assertEquals(serviceService.getServicesForApprover(0, 10, ServiceStatus.OPEN.toString(), 3), ret);
    }

    @Test
    public void addServiceException(){
        expectedEx.expect(RequestException.class);
        expectedEx.expectMessage("Status of new service must be draft or open");

        serviceService.addService(serviceCreateForm);
    }

    @Test
    public void reviewService() throws Exception {
        //TODO: add actual tests
        Assert.assertNull(null);
    }

}