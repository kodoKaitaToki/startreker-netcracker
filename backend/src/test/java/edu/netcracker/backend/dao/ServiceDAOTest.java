package edu.netcracker.backend.dao;

import edu.netcracker.backend.dao.impl.ServiceDAOImpl;
import edu.netcracker.backend.dao.mapper.ServiceMapper;
import edu.netcracker.backend.message.response.ServiceDTO;
import edu.netcracker.backend.model.ServiceDescr;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceDAOTest {
    @Mock
    JdbcTemplate jdbcTemplate;

    @InjectMocks
    ServiceDAOImpl serviceDAOImpl;

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

        when(jdbcTemplate.query(anyString(), (Object[]) any(), any(ServiceMapper.class)))
                .thenReturn(ret);

        Assert.assertEquals(serviceDAOImpl.getServicesForApprover(0, 10, 2), ret);
    }

    @Test
    public void getServicesForApproverId() throws Exception {

        when(jdbcTemplate.query(anyString(), (Object[]) any(), any(ServiceMapper.class)))
                .thenReturn(ret);

        Assert.assertEquals(serviceDAOImpl.getServicesForApprover(0, 10, 2, 2), ret);
    }

}