package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.request.MandatoryTimeInterval;
import edu.netcracker.backend.message.request.OptionalTimeInterval;
import edu.netcracker.backend.message.request.ServiceCreateForm;
import edu.netcracker.backend.message.response.*;
import edu.netcracker.backend.security.SecurityContext;
import edu.netcracker.backend.service.ServiceService;
import edu.netcracker.backend.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j(topic = "log")
@RequestMapping("/api/v1/service")
public class ServiceController {

    private final StatisticsService statisticsService;
    private final SecurityContext securityContext;

    private final ServiceService serviceService;

    @Autowired
    public ServiceController(StatisticsService statisticsService,
                             SecurityContext securityContext,
                             ServiceService serviceService) {
        this.statisticsService = statisticsService;
        this.securityContext = securityContext;
        this.serviceService = serviceService;
    }

    @RequestMapping(value = "/distribution", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<ServiceDistributionElement> getServiceStatistics() {
        return statisticsService.getServiceStatistics();
    }

    @RequestMapping(value = "/sales", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public CarrierRevenueResponse getServicesSalesStatistics(OptionalTimeInterval timeInterval) {
        return timeInterval != null && timeInterval.isProvided()
                ? statisticsService.getServicesSalesStatistics(securityContext.getUser()
                                                                              .getUserId(),
                                                               timeInterval.getFrom(),
                                                               timeInterval.getTo())
                : statisticsService.getServicesSalesStatistics(securityContext.getUser()
                                                                              .getUserId());
    }

    @RequestMapping(value = "/sales/per_week", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierRevenueResponse> getServicesSalesStatisticsByWeek(@Valid MandatoryTimeInterval timeInterval) {
        return statisticsService.getServicesSalesStatisticsByWeek(securityContext.getUser()
                                                                                 .getUserId(),
                                                                  timeInterval.getFrom(),
                                                                  timeInterval.getTo());
    }

    @RequestMapping(value = "/sales/per_month", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierRevenueResponse> getServicesSalesStatisticsByMonth(@Valid MandatoryTimeInterval timeInterval) {
        return statisticsService.getServicesSalesStatisticsByMonth(securityContext.getUser()
                                                                                  .getUserId(),
                                                                   timeInterval.getFrom(),
                                                                   timeInterval.getTo());
    }

    @RequestMapping(value = "/views/per_week", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierViewsResponse> getServiceViewsStatisticsByWeek(@Valid MandatoryTimeInterval timeInterval) {
        return statisticsService.getServiceViewsStatisticsByWeek(securityContext.getUser()
                                                                                .getUserId(),
                                                                 timeInterval.getFrom(),
                                                                 timeInterval.getTo());
    }

    @RequestMapping(value = "/views/per_month", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierViewsResponse> getServiceViewsStatisticsByMonth(@Valid MandatoryTimeInterval timeInterval) {
        return statisticsService.getServiceViewsStatisticsByMonth(securityContext.getUser()
                                                                                 .getUserId(),
                                                                  timeInterval.getFrom(),
                                                                  timeInterval.getTo());
    }

    @RequestMapping(value = "/{id}/views/per_week", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierViewsResponse> getServiceViewsStatisticsByServiceByWeek(@Valid MandatoryTimeInterval timeInterval,
                                                                               @PathVariable("id") Long serviceId) {
        return statisticsService.getServiceViewsStatisticsByServiceByWeek(securityContext.getUser(),
                                                                          serviceId,
                                                                          timeInterval.getFrom(),
                                                                          timeInterval.getTo());
    }

    @RequestMapping(value = "/{id}/views/per_month", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierViewsResponse> getServiceViewsStatisticsByServiceByMonth(@Valid MandatoryTimeInterval timeInterval,
                                                                                @PathVariable("id") Long serviceId) {
        return statisticsService.getServiceViewsStatisticsByServiceByMonth(securityContext.getUser(),
                                                                           serviceId,
                                                                           timeInterval.getFrom(),
                                                                           timeInterval.getTo());
    }

    @RequestMapping(value = "/preload", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<ServicePreload> preloadServices() {
        return serviceService.preloadForCarrier(securityContext.getUser());
    }

    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public ServiceCRUDDTO updateService(@Valid @RequestBody ServiceCRUDDTO serviceCRUDDTO) {
        log.debug("ServiceController.updateService(ServiceCRUDDTO serviceCRUDDTO) was invoked "
                  + "to update a service with id={}", serviceCRUDDTO.getId());
        return serviceService.updateService(serviceCRUDDTO);
    }

    @RequestMapping(value = "/amount", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public Integer getServicesAmount(@RequestParam("status") String status) {
        log.debug("ServiceController.getServicesAmount(String status) was invoked with status={}", status);
        return serviceService.getCarrierServicesAmount(status);
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public ServiceCRUDDTO addService(@Valid @RequestBody ServiceCreateForm serviceCreateForm) {
        log.debug("ServiceController.addService(ServiceCreateForm serviceCreateForm) was invoked "
                  + "to add a new service with name={}, status={}",
                  serviceCreateForm.getServiceName(),
                  serviceCreateForm.getServiceStatus());
        return serviceService.addService(serviceCreateForm);
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ROLE_CARRIER') or hasAuthority('ROLE_APPROVER')")
    public List<ServiceCRUDDTO> getServices(@RequestParam("from") int from,
                                                       @RequestParam("number") int number,
                                                       @RequestParam("status") String status) {
        log.debug("ServiceController.getServices(int from,\n" +
                "                                                       int number,\n" +
                "                                                       String status) was invoked " +
                "with parameters status={}, from={}, number={}", status, from, number);

        return serviceService.getServices(from, number, status);
    }

    @RequestMapping(value = "/{id}/review", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('ROLE_APPROVER')")
    public ServiceCRUDDTO updateServiceReview(@Valid @RequestBody ServiceCRUDDTO serviceCRUDDTO,
                                              @PathVariable("id") Long serviceId) {
        int approverId = securityContext.getUser()
                                        .getUserId();
        return serviceService.reviewService(serviceCRUDDTO, approverId);
    }

}
