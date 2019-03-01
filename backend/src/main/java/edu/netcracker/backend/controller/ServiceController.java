package edu.netcracker.backend.controller;

import edu.netcracker.backend.dao.StatisticsDAO;
import edu.netcracker.backend.message.request.ServiceCreateForm;
import edu.netcracker.backend.message.response.ServiceDTO;
import edu.netcracker.backend.message.response.ServiceDistributionElement;
import edu.netcracker.backend.model.ServiceDescr;
import edu.netcracker.backend.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ServiceController {

    private final StatisticsDAO statisticsDAO;
    private final ServiceService serviceService;

    @Autowired
    public ServiceController(StatisticsDAO statisticsDAO,
                             ServiceService serviceService) {

        this.statisticsDAO = statisticsDAO;
        this.serviceService = serviceService;
    }

    @PostMapping("api/service/distribution")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<ServiceDistributionElement> getRouteStatistics(){
        return statisticsDAO.getServicesDistribution();
    }

    @GetMapping("api/v1/carrier/service")
    //@PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<ServiceDescr> getAllServices(){
        return serviceService.getServicesOfCarrier();
    }

    @GetMapping("api/v1/carrier/service/pagin")
    //@PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<ServiceDescr> getPaginServices(@RequestParam("from") Integer from, @RequestParam("number") Integer number){
        return serviceService.getPaginServicesOfCarrier(from, number);
    }

    @DeleteMapping("api/v1/carrier/service/{servId}")
    //@PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public ServiceDescr deleteService(@PathVariable Long servId){
        return serviceService.deleteService(servId);
    }

    @PutMapping("api/v1/carrier/service")
    //@PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public ServiceDescr updateService(@Valid @RequestBody ServiceDescr serviceDescr){
        return serviceService.updateService(serviceDescr);
    }

    @PostMapping("api/v1/carrier/service")
    //@PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public ServiceDTO addService(@Valid @RequestBody ServiceCreateForm serviceCreateForm){
        return serviceService.addService(serviceCreateForm);
    }

}
