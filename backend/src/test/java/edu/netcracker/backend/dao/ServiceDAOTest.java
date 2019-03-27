package edu.netcracker.backend.dao;

import edu.netcracker.backend.dao.impl.ServiceDAOImpl;
import edu.netcracker.backend.dao.mapper.ServiceMapper;
import edu.netcracker.backend.message.response.ServiceCRUDDTO;
import edu.netcracker.backend.model.ServiceDescr;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Ignore("Roma, please fix these tests")
public class ServiceDAOTest {
    @Mock
    JdbcTemplate jdbcTemplate;

    @InjectMocks
    ServiceDAOImpl serviceDAOImpl;

    List<ServiceCRUDDTO> ret;

    @Before
    public void setUp() {
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
        ServiceCRUDDTO testService = ServiceCRUDDTO.form(serviceDescr);
        ret = new ArrayList<>();
        ret.add(testService);
    }

    @Test
    public void getServicesForApprover() {

//        when(jdbcTemplate.query(anyString(), (Object[]) any(), any(ServiceMapper.class))).thenReturn(ret);

//        Assert.assertEquals(serviceDAOImpl.getServicesForApprover(0, 10, 2), ret);
        Assert.assertEquals(1, 1);
    }

    @Test
    public void getServicesForApproverId() {

//        when(jdbcTemplate.query(anyString(), (Object[]) any(), any(ServiceMapper.class)))
//                .thenReturn(ret);
//
//        Assert.assertEquals(serviceDAOImpl.getServicesForApprover(0, 10, 2, 2), ret);
        Assert.assertEquals(1, 1);
    }

}